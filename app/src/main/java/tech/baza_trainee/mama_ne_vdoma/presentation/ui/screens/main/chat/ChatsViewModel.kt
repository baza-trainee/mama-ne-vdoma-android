package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.domain.socket.SocketManager
import tech.baza_trainee.mama_ne_vdoma.domain.socket.SocketManager.Companion.ERROR_TAG
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupChatModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInChatModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class ChatsViewModel(
    private val navigator: PageNavigator,
    private val socketManager: SocketManager,
    private val userProfileRepository: UserProfileRepository,
    private val groupsRepository: GroupsRepository,
    private val filesRepository: FilesRepository,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(ChatsViewState())
    val viewState: StateFlow<ChatsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<RequestState>(RequestState.Idle)
    val uiState: State<RequestState>
        get() = _uiState

    init {
        viewModelScope.launch {
            socketManager.messages.collect { list ->
                _viewState.update { state ->
                    val id = list.firstOrNull()?.chatId.orEmpty()
                    val groupChat = state.groupChats[id]
                    val newMessages = list + groupChat?.messages.orEmpty()
                    val newGroupChat = groupChat?.copy(
                        id = id,
                        messages = newMessages
                    ) ?: GroupChatModel(
                        id = id,
                        messages = newMessages
                    )

                    state.copy(
                        groupChats = state.groupChats.add(id, newGroupChat).also {
                            checkUnreadCount(id)
                        }
                    )
                }
            }
        }


        viewModelScope.launch {
            socketManager.message.collect { message ->
                if (message.id == ERROR_TAG) {
                    _uiState.value = RequestState.OnError(message.message)
                } else {
                    _viewState.update { state ->
                        val id = message.chatId
                        val groupChat = state.groupChats[id]
                        val newMessages = groupChat?.messages.orEmpty() + listOf(message)
                        val newGroupChat = groupChat?.copy(
                            id = id,
                            messages = newMessages
                        ) ?: GroupChatModel(
                            id = id,
                            messages = newMessages
                        )

                        state.copy(
                            groupChats = state.groupChats.add(id, newGroupChat).also {
                                checkUnreadCount(id)
                            }
                        )
                    }
                }
            }
        }

        socketManager.connect()

        getUserInfo()
    }

    override fun onCleared() {
        super.onCleared()

        if (socketManager.isConnected())
            socketManager.disconnect()
    }

    fun handleEvent(event: ChatsScreenEvent) {
        when(event) {
            ChatsScreenEvent.OnBack -> {
                if (_viewState.value.selectedChat.isNotEmpty())
                    _viewState.update {
                        it.copy(
                            selectedChat = ""
                        )
                    }
                else
                    navigator.goToPrevious()
            }
            is ChatsScreenEvent.GoToChat -> {
                _viewState.update {
                    it.copy(
                        selectedChat = event.id
                    )
                }
            }

            ChatsScreenEvent.SendMessage -> sendMessage()
            is ChatsScreenEvent.SetMessage -> {
                _viewState.update {
                    it.copy(
                        userMessage = event.message
                    )
                }
            }

            is ChatsScreenEvent.SetLastViewed -> {
                val messages = userPreferencesDatastoreManager.messages.toMutableSet()
                messages.apply {
                    removeIf { it.contains(event.chatId) }
                    add("${event.chatId};${event.messageId}")
                }
                userPreferencesDatastoreManager.messages = messages
            }

            ChatsScreenEvent.OnLoadMore -> loadMoreMessages()

            ChatsScreenEvent.ResetUiState -> _uiState.value = RequestState.Idle
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { user ->
                _viewState.update {
                    it.copy(myId = user.id)
                }

                getGroupsForParent(user.id)
            }
            onError { _uiState.value = RequestState.OnError(it) }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }

    private fun getGroupsForParent(id: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(id)
            }
            onSuccess { groups ->
                groups.forEach { entity ->
                    socketManager.getMessages(entity.id)

                    getGroupAvatar(entity.id, entity.avatar)

                    getMemberInfo(entity.id, entity.members.map { it.parentId })

                    _viewState.update { state ->
                        val groupChat = state.groupChats[id]?.copy(
                            id = entity.id,
                            name = entity.name
                        ) ?: GroupChatModel(
                            id = entity.id,
                            name = entity.name
                        )
                        state.copy(
                            groupChats = state.groupChats.add(entity.id, groupChat)
                        )
                    }
                }
            }
            onError { _uiState.value = RequestState.OnError(it) }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(isLoading = isLoading)
                }
            }
        }
    }

    private fun getGroupAvatar(id: String, avatar: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatar) }
            onSuccess {
                _viewState.update { state ->
                    val groupChat = state.groupChats[id]?.copy(
                        id = id,
                        avatar = it
                    ) ?: GroupChatModel(
                        id = id,
                        avatar = it
                    )
                    state.copy(
                        groupChats = state.groupChats.add(id, groupChat)
                    )
                }
            }
        }
    }

    private fun getMemberInfo(groupId: String, ids: List<String>) {
        viewModelScope.launch {
            val members = mutableSetOf<ParentInChatModel>()
            ids.forEach { id ->
                val result1 = userProfileRepository.getUserById(id)
                val member = getResult(result1) ?: UserProfileEntity()

                val result2 = filesRepository.getAvatar(member.avatar)
                val avatar = getResult(result2) ?: Uri.EMPTY

                members.add(
                    ParentInChatModel(
                        id = id,
                        name = member.name,
                        avatar = avatar
                    )
                )

                _viewState.update { state ->
                    val groupChat = state.groupChats[groupId]?.copy(
                        id = groupId,
                        members = members
                    ) ?: GroupChatModel(
                        id = groupId,
                        members = members
                    )
                    state.copy(
                        groupChats = state.groupChats.add(groupId, groupChat)
                    )
                }
            }
        }
    }

    private fun sendMessage() {
        socketManager.sendMessage(
            groupId = _viewState.value.selectedChat,
            message = _viewState.value.userMessage
        )

        _viewState.update {
            it.copy(userMessage = "")
        }
    }

    private fun checkUnreadCount(groupId: String) {
        _viewState.update { state ->
            val messages = _viewState.value.groupChats[groupId]?.messages?.map { it.id }.orEmpty()
            val lastRead = userPreferencesDatastoreManager.messages
                .find { it.contains(groupId) }
                .orEmpty()

            val unreadCount = if (lastRead.isEmpty()) messages.size
            else {
                val lastReadId = lastRead.split(";")[1]
                val indexOfLastRead = messages.indexOf(lastReadId)
                messages.lastIndex - indexOfLastRead
            }

            val groupChat = state.groupChats[groupId]?.copy(
                id = groupId,
                unread = unreadCount
            ) ?: GroupChatModel(
                id = groupId,
                unread = unreadCount
            )
            state.copy(
                groupChats = state.groupChats.add(groupId, groupChat)
            )
        }
    }

    private fun loadMoreMessages() = with(_viewState.value){
        socketManager.getMessages(
            groupId = selectedChat,
            startDate = groupChats[selectedChat]
                ?.messages
                ?.firstOrNull()
                ?.createdAt
                .orEmpty()
        )
    }

    private fun <T> getResult(result: RequestResult<T>): T? {
        return when(result) {
            is RequestResult.Error -> {
                _uiState.value = RequestState.OnError(result.error)
                null
            }

            is RequestResult.Success -> result.result
        }
    }

    private fun <K, V> SnapshotStateMap<K, V>.add(key: K, value: V) = apply {
        if (containsKey(key))
            replace(key, value)
        else
            put(key, value)
    }
}