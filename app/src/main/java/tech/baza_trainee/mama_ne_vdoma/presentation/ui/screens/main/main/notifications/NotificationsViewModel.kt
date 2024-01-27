package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.JoinRequestEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.MemberEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.NotificationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.NotificationsUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class NotificationsViewModel(
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: PageNavigator,
    private val mainNavigator: ScreenNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(NotificationsViewState())
    val viewState: StateFlow<NotificationsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<NotificationsUiState>(NotificationsUiState.Idle)
    val uiState: State<NotificationsUiState>
        get() = _uiState

    init {
        getUserInfo()
    }

    fun handleEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.ResetUiState -> _uiState.value = NotificationsUiState.Idle
            NotificationsEvent.OnBack -> navigator.goToPrevious()
            is NotificationsEvent.AcceptUser -> acceptJoinRequest(event.group, event.child)
            is NotificationsEvent.DeclineUser -> declineJoinRequest(event.group, event.child)
            is NotificationsEvent.CancelRequest -> cancelRequest(event.group, event.child)
            NotificationsEvent.ClearNotifications -> clearNotifications()
            NotificationsEvent.GoToMain -> navigator.navigate(MainScreenRoutes.Main)
            NotificationsEvent.MyGroups -> navigator.navigate(GroupsScreenRoutes.Groups)
            NotificationsEvent.SearchGroup -> mainNavigator.navigate(StandaloneGroupsRoutes.ChooseChild.getDestination(isForSearch = true))
        }
    }

    private fun acceptJoinRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.acceptRequest(group, child)
            }
            onSuccess {
                _uiState.value = NotificationsUiState.OnAccepted
                getUserInfo()
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private fun cancelRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.cancelRequest(group, child)
            }
            onSuccess { getUserInfo() }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private fun declineJoinRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.declineRequest(group, child)
            }
            onSuccess { getUserInfo() }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute { userAuthRepository.getUserInfo() }
            onSuccess { entity ->
                getGroupsForParent(entity.id)

                if (entity.groupJoinRequests.isNotEmpty())
                    getGroupsForJoin(entity.groupJoinRequests)
                else
                    _viewState.update {
                        it.copy(myJoinRequests = emptyList())
                    }

                if (entity.notifications.isNotEmpty())
                    getGroupsForNotifications(entity.notifications)
                else
                    _viewState.update {
                        it.copy(notifications = emptyList())
                    }

                preferencesDatastoreManager.myJoinRequests = entity.groupJoinRequests.size
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private fun getGroupsForJoin(requests: List<JoinRequestEntity>) {
        viewModelScope.launch {
            setProgress(true)

            val requestsList = requests.map { entity ->
                async {
                    getGroup(entity)
                }
            }

            val joinRequests = mutableListOf<JoinRequestUiModel>()

            requestsList.awaitAll().onEach {
                val membersList = it.group.members.map {
                    async {
                        getMember(it, false)
                    }
                }

                joinRequests.add(
                    it.copy(
                        group = it.group.copy(members = membersList.awaitAll())
                    )
                )
            }

            _viewState.update {
                it.copy(
                    myJoinRequests = joinRequests,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getGroup(request: JoinRequestEntity): JoinRequestUiModel {
        setProgress(true)

        val result = groupsRepository.getGroupById(request.groupId)
        val group = getResult(result) ?: GroupEntity()

        val avatar = filesRepository.getAvatar(group.avatar)
        val location =
            locationRepository.getAddressFromLocation(
                latLng = LatLng(
                    group.location.coordinates[1],
                    group.location.coordinates[0]
                )
            )

        val members = group.members
            .groupBy { it.parentId }
            .map { (parentId, children) ->
                MemberUiModel(id = parentId, children = children.map { it.childId }.toList())
            }

        return JoinRequestUiModel(
            group = GroupUiModel(
                id = group.id,
                name = group.name,
                ages = group.ages,
                avatar = getResult(avatar) ?: Uri.EMPTY,
                address = getResult(location).orEmpty(),
                members = members
            ),
            child = ChildEntity(childId = request.childId)
        )
    }

    private suspend fun getMember(user: MemberUiModel, isMine: Boolean): MemberUiModel {
        setProgress(true)

        val result1 = userProfileRepository.getUserById(user.id)
        val member = getResult(result1) ?: UserProfileEntity()

        val result2 = filesRepository.getAvatar(member.avatar)
        val avatar = getResult(result2) ?: Uri.EMPTY

        var newUser = user.copy(
            id = member.id,
            name = member.name,
            avatar = avatar
        )

        if (isMine)
            newUser = newUser.copy(
                phone = "${member.countryCode}${member.phone}",
                email = member.email
            )

        return newUser
    }

    private fun getGroupsForParent(parent: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess { entityList ->
                val myGroups = entityList.filter { it.adminId == parent }
                if (myGroups.flatMap { it.askingJoin }.isNotEmpty()) {
                    fetchAdminJoinRequests(myGroups)
                } else
                    _viewState.update {
                        it.copy(adminJoinRequests = emptyList())
                    }

                preferencesDatastoreManager.adminJoinRequests = myGroups.flatMap { it.askingJoin }.size
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading(::setProgress)
        }
    }

    private fun fetchAdminJoinRequests(entityList: List<GroupEntity>) {
        viewModelScope.launch {
            setProgress(true)

            val joinRequests = mutableListOf<List<JoinRequestUiModel>>()

            entityList.map { entity ->
                if (entity.askingJoin.isNotEmpty()) {
                    val request = entity.askingJoin.map {
                        async {
                            getUserAndChild(entity.id, it)
                        }
                    }
                    joinRequests.add(request.awaitAll())
                }
            }

            _viewState.update {
                it.copy(
                    adminJoinRequests = joinRequests.flatten(),
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getUserAndChild(groupId: String, member: MemberEntity): JoinRequestUiModel {
        setProgress(true)

        val result1 = userProfileRepository.getUserById(member.parentId)
        val parent = getResult(result1) ?: UserProfileEntity()

        val result2 = groupsRepository.getGroupFullInfo(groupId)
        val groupEntity = getResult(result2) ?: GroupFullInfoEntity()

        val result3 = filesRepository.getAvatar(parent.avatar)
        val avatar = getResult(result3) ?: Uri.EMPTY

        val address = if (parent.location.coordinates.isNotEmpty()) {
            val result4 = locationRepository.getAddressFromLocation(
                latLng = LatLng(
                    parent.location.coordinates[1],
                    parent.location.coordinates[0]
                )
            )
            getResult(result4).orEmpty()
        } else ""

        return JoinRequestUiModel(
            group = GroupUiModel(
                id = groupEntity.group.id,
                name = groupEntity.group.name
            ),
            parentId = parent.id,
            parentName = parent.name,
            parentEmail = parent.email,
            parentPhone = "${parent.countryCode}${parent.phone}",
            parentAvatar = avatar,
            parentAddress = address,
            child = groupEntity.children.find { it.childId == member.childId } ?: ChildEntity()
        )
    }

    private fun getGroupsForNotifications(notifications: List<NotificationEntity>) {
        viewModelScope.launch {
            setProgress(true)

            val requestsList = notifications.map { entity ->
                async {
                    getGroupForNotification(entity)
                }
            }

            val notificationsUi = mutableListOf<NotificationsUiModel>()

            requestsList.awaitAll().onEach { uiModel ->
                notificationsUi.add(
                    NotificationsUiModel(
                        group = uiModel,
                        type = notifications.find { it.groupId == uiModel.id }?.type.orEmpty()
                    )
                )
            }

            _viewState.update {
                it.copy(
                    notifications = notificationsUi,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getGroupForNotification(request: NotificationEntity): GroupUiModel {
        setProgress(true)

        val result = groupsRepository.getGroupById(request.groupId)
        val group = getResult(result) ?: GroupEntity()

        return GroupUiModel(
            id = group.id,
            name = group.name
        )
    }

    private fun clearNotifications() {
        networkExecutor {
            execute { userProfileRepository.deleteUserNotifications() }
            onSuccess { getUserInfo() }
            onError { _uiState.value = NotificationsUiState.OnError(it) }
            onLoading(::setProgress)
        }
    }

    private fun <T> getResult(result: RequestResult<T>): T? {
        return when(result) {
            is RequestResult.Error -> {
                _uiState.value = NotificationsUiState.OnError(result.error)
                null
            }

            is RequestResult.Success -> result.result
        }
    }

    private fun setProgress(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }
}