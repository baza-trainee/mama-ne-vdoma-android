package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupFullInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.JoinRequestEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

class NotificationsViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val groupsRepository: GroupsRepository,
    private val locationRepository: LocationRepository,
    private val navigator: PageNavigator,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): ViewModel() {

    private val _viewState = MutableStateFlow(NotificationsViewState())
    val viewState: StateFlow<NotificationsViewState> = _viewState.asStateFlow()

    private val _uiState = mutableStateOf<NotificationsUiState>(NotificationsUiState.Idle)
    val uiState: State<NotificationsUiState>
        get() = _uiState

    private val groupAvatarFlow = MutableStateFlow( "" to Uri.EMPTY)
    private val groupsFlow = MutableStateFlow(Triple("", GroupEntity(), ""))
    private val membersFlow = MutableStateFlow("" to UserProfileEntity())
    private val memberAvatarsFlow = MutableStateFlow(Triple("", "", Uri.EMPTY))

    private val childrenFlow = MutableStateFlow(Triple("", "", GroupFullInfoEntity()))
    private val userFlow = MutableStateFlow("" to UserProfileEntity())
    private val locationsFlow = MutableStateFlow("" to "")

    init {
        getUserInfo()

        viewModelScope.launch {
            combine(locationsFlow, userFlow, childrenFlow) { location, user, child ->
                val currentRequests = _viewState.value.adminJoinRequests.toMutableList()

                if (location.first.isNotEmpty()) {
                    var currentUiModel = currentRequests.find { it.group.id == location.first } ?: JoinRequestUiModel()
                    val index = currentRequests.indexOf(currentUiModel)
                    val newGroup = currentUiModel.group.copy(id = location.first)
                    currentUiModel = currentUiModel.copy(
                        group = newGroup,
                        parentAddress = location.second
                    )
                    if (index == -1)
                        currentRequests.add(currentUiModel)
                    else {
                        currentRequests.removeAt(index)
                        currentRequests.add(index, currentUiModel)
                    }
                }

                if (user.first.isNotEmpty()) {
                    var currentUiModel = currentRequests.find { it.group.id == user.first } ?: JoinRequestUiModel()
                    val index = currentRequests.indexOf(currentUiModel)
                    val newGroup = currentUiModel.group.copy(id = user.first)
                    currentUiModel = currentUiModel.copy(
                        group = newGroup,
                        parentId = user.second.id,
                        parentName = user.second.name,
                        parentEmail = user.second.email,
                        parentPhone = "${user.second.countryCode}${user.second.phone}",
                    )
                    if (index == -1)
                        currentRequests.add(currentUiModel)
                    else {
                        currentRequests.removeAt(index)
                        currentRequests.add(index, currentUiModel)
                    }
                }

                if (child.first.isNotEmpty()) {
                    val currentChild = child.third.children.find { it.childId == child.second } ?: ChildEntity()
                    var currentRequest = currentRequests.find { it.group.id == child.first } ?: JoinRequestUiModel()
                    val indexOfRequest = currentRequests.indexOf(currentRequest)
                    val newGroup = currentRequest.group.copy(id = child.first)
                    currentRequest = currentRequest.copy(
                        group = newGroup,
                        child = currentChild.copy(childId = child.second)
                    )
                    currentRequests.apply {
                        if (indexOfRequest == -1)
                            add(currentRequest)
                        else {
                            removeAt(indexOfRequest)
                            add(indexOfRequest, currentRequest)
                        }
                    }
                }

                currentRequests
            }.collect { list ->
                _viewState.update {
                    it.copy(
                        adminJoinRequests = list
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(groupAvatarFlow, membersFlow, groupsFlow, memberAvatarsFlow) { avatar, member, group, memberAvatar ->
                val currentRequests = _viewState.value.myJoinRequests.toMutableList()

                if (group.first.isNotEmpty()) {
                    var currentRequest = currentRequests.find { it.group.id == group.first } ?: JoinRequestUiModel()
                    val indexOfRequest = currentRequests.indexOf(currentRequest)
                    val uiModel = GroupUiModel(
                        id = group.second.id,
                        adminId = group.second.adminId,
                        name = group.second.name,
                        description = group.second.description,
                        ages = group.second.ages
                    )
                    currentRequest = currentRequest.copy(
                        group = uiModel,
                        parentAvatar = preferencesDatastoreManager.avatarUri,
                        parentName = preferencesDatastoreManager.name,
                        parentId = preferencesDatastoreManager.id,
                        child = ChildEntity(childId = group.third)
                    )
                    currentRequests.apply {
                        if (indexOfRequest == -1)
                            add(currentRequest)
                        else {
                            removeAt(indexOfRequest)
                            add(indexOfRequest, currentRequest)
                        }
                    }
                }

                if (avatar.first.isNotEmpty()) {
                    var currentRequest = currentRequests.find { it.group.id == avatar.first } ?: JoinRequestUiModel()
                    val indexOfRequest = currentRequests.indexOf(currentRequest)
                    val newGroup = currentRequest.group.copy(
                        id = avatar.first,
                        avatar = avatar.second
                    )

                    currentRequest = currentRequest.copy(
                        group = newGroup
                    )
                    currentRequests.apply {
                        if (indexOfRequest == -1)
                            add(currentRequest)
                        else {
                            removeAt(indexOfRequest)
                            add(indexOfRequest, currentRequest)
                        }
                    }
                }

                if (member.first.isNotEmpty()) {
                    var currentRequest =
                        currentRequests.find { it.group.id == member.first } ?: JoinRequestUiModel()
                    val indexOfRequest = currentRequests.indexOf(currentRequest)
                    var newGroup = currentRequest.group

                    val currentMembers = newGroup.members.toMutableList()
                    var currentMember = currentMembers.find { it.id == member.second.id } ?: MemberUiModel()
                    val index = currentMembers.indexOf(currentMember)

                    currentMember = currentMember.copy(
                        id = member.second.id,
                        name = member.second.name
                    )
                    currentMembers.apply {
                        if (index == -1)
                            add(currentMember)
                        else {
                            removeAt(index)
                            add(index, currentMember)
                        }
                    }

                    newGroup = newGroup.copy(
                        id = member.first,
                        members = currentMembers
                    )

                    currentRequest = currentRequest.copy(
                        group = newGroup
                    )
                    currentRequests.apply {
                        if (indexOfRequest == -1)
                            add(currentRequest)
                        else {
                            removeAt(indexOfRequest)
                            add(indexOfRequest, currentRequest)
                        }
                    }
                }

                if (memberAvatar.first.isNotEmpty()) {
                    var currentRequest = currentRequests.find { it.group.id == memberAvatar.first } ?: JoinRequestUiModel()
                    val indexOfRequest = currentRequests.indexOf(currentRequest)
                    var newGroup = currentRequest.group.copy()
                    val currentMembers = newGroup.members.toMutableList()
                    var currentMember = currentMembers.find { it.id == memberAvatar.second } ?: MemberUiModel()
                    val index = currentMembers.indexOf(currentMember)
                    currentMember = currentMember.copy(avatar = memberAvatar.third)
                    currentMembers.apply {
                        if (index == -1)
                            add(currentMember)
                        else {
                            removeAt(index)
                            add(index, currentMember)
                        }
                    }
                    newGroup = newGroup.copy(
                        id = memberAvatar.first,
                        members = currentMembers
                    )

                    currentRequest = currentRequest.copy(
                        group = newGroup
                    )
                    currentRequests.apply {
                        if (indexOfRequest == -1)
                            add(currentRequest)
                        else {
                            removeAt(indexOfRequest)
                            add(indexOfRequest, currentRequest)
                        }
                    }
                }

                currentRequests
            }.collect { list ->
                _viewState.update {
                    it.copy(
                        myJoinRequests = list
                    )
                }
            }
        }
    }

    fun handleEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.ResetUiState -> _uiState.value = NotificationsUiState.Idle
            NotificationsEvent.OnBack -> navigator.goToPrevious()
            is NotificationsEvent.AcceptUser -> acceptJoinRequest(event.group, event.child)
            is NotificationsEvent.DeclineUser -> declineJoinRequest(event.group, event.child)
            is NotificationsEvent.CancelRequest -> cancelRequest(event.group, event.child)
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
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun cancelRequest(group: String, child: String) {
        networkExecutor {
            execute {
                groupsRepository.cancelRequest(group, child)
            }
            onSuccess {
                getUserInfo()
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
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
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserInfo() {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserInfo()
            }
            onSuccess { entity ->
                getGroups(entity.id)

                if (entity.groupJoinRequests.isNotEmpty())
                    entity.groupJoinRequests.forEach(::getGroupById)
                else
                    _viewState.update {
                        it.copy(myJoinRequests = emptyList())
                    }

                preferencesDatastoreManager.myJoinRequests = entity.groupJoinRequests.size
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getGroupById(joinRequest: JoinRequestEntity) {
        networkExecutor<GroupEntity> {
            execute {
                groupsRepository.getGroupById(joinRequest.groupId)
            }
            onSuccess { entity ->
                getGroupAvatar(entity.avatar, joinRequest.groupId)

                if (entity.location.coordinates.isNotEmpty())
                    getAddressFromLocation(
                        latLng = LatLng(
                            entity.location.coordinates[1],
                            entity.location.coordinates[0]
                        ),
                        joinRequest.groupId
                    )

                entity.members.map { it.parentId }.forEach {
                    getUser(it, joinRequest.groupId)
                }

                groupsFlow.update {
                    Triple(joinRequest.groupId, entity, joinRequest.childId)
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getGroups(parent: String) {
        networkExecutor<List<GroupEntity>> {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess { entityList ->
                val joinRequests = mutableListOf<JoinRequestUiModel>()
                if (entityList.flatMap { it.askingJoin }.isNotEmpty()) {
                    entityList.onEach { group ->
                        if (group.askingJoin.isNotEmpty())
                            group.askingJoin.forEach { member ->
                                getUserForJoin(member.parentId, group.id)
                                getChild(member.childId, group.id)
                                joinRequests.add(
                                    JoinRequestUiModel(
                                        group = GroupUiModel(
                                            id = group.id,
                                            name = group.name
                                        ),
                                        parentId = member.parentId
                                    )
                                )
                            }
                    }

                    _viewState.update {
                        it.copy(
                            adminJoinRequests = joinRequests
                        )
                    }
                } else
                    _viewState.update {
                        it.copy(adminJoinRequests = emptyList())
                    }

                preferencesDatastoreManager.adminJoinRequests =
                    entityList.flatMap { it.askingJoin }.size
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserForJoin(userId: String, groupId: String) {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                userFlow.update {
                    Pair(groupId, user)
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUser(userId: String, groupId: String) {
        networkExecutor<UserProfileEntity> {
            execute {
                userProfileRepository.getUserById(userId)
            }
            onSuccess { user ->
                getUserAvatar(user.avatar, groupId, user.id)

                membersFlow.update {
                    groupId to user
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getUserAvatar(avatarId: String, groupId: String, userId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                memberAvatarsFlow.update {
                    Triple(groupId, userId, uri)
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getGroupAvatar(avatarId: String, groupId: String) {
        networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                groupAvatarFlow.update {
                    groupId to uri
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getChild(childId: String, groupId: String) {
        networkExecutor {
            execute {
                groupsRepository.getGroupFullInfo(groupId)
            }
            onSuccess { groupFullInfo ->
                childrenFlow.update {
                    Triple(groupId, childId, groupFullInfo)
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng, groupId: String) {
        networkExecutor {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess { address ->
                locationsFlow.update {
                    groupId to address
                }
            }
            onError { error ->
                _uiState.value = NotificationsUiState.OnError(error)
            }
            onLoading { isLoading ->
                _viewState.update {
                    it.copy(
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}