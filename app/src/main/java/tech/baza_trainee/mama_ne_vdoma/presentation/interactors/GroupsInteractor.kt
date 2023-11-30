package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.GroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UpdateGroupEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.MemberUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

interface GroupsInteractor {

    val groupsFlow: StateFlow<List<GroupUiModel>>

    fun setGroupsCoroutineScope(coroutineScope: CoroutineScope)

    fun setGroupsNetworkListener(listener: NetworkEventsListener)

    fun startFetching(entities: List<GroupEntity>, isMine: Boolean = false)

    fun getGroups(parent: String, onSuccess: (List<GroupEntity>) -> Unit)

    fun leaveGroup(group: String, onSuccess: () -> Unit)

    fun deleteGroup(group: String, onSuccess: () -> Unit)

    fun kickUser(group: String, child: String, onSuccess: () -> Unit)

    fun switchAdmin(group: String, member: String, onSuccess: () -> Unit)

    fun updateGroupLocation(location: LatLng, group: String, onSuccess: () -> Unit)

    fun createGroup(
        child: String,
        name: String,
        description: String,
        onSuccess: (GroupEntity) -> Unit
    )

    fun updateGroup(
        group: String,
        name: String,
        description: String,
        ages: String,
        avatar: String,
        schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
        onSuccess: () -> Unit
    )

    fun getGroupLocation(address: String, onSuccess: (LatLng?) -> Unit)

    fun uploadGroupAvatar(image: Bitmap, onSuccess: (String) -> Unit)

    fun getGroupAddress(latLng: LatLng, onSuccess: (String) -> Unit)
}

class GroupsInteractorImpl(
    private val groupsRepository: GroupsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val locationRepository: LocationRepository,
    private val filesRepository: FilesRepository
) : GroupsInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener

    private val _groupsFlow = MutableStateFlow<List<GroupUiModel>>(emptyList())
    override val groupsFlow: StateFlow<List<GroupUiModel>> = _groupsFlow.asStateFlow()

    override fun setGroupsCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun setGroupsNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun startFetching(entities: List<GroupEntity>, isMine: Boolean) {
        coroutineScope.launch {
            networkListener.onLoading(true)

            val groupsList = entities.map { entity ->
                async {
                    getAvatarsAndAddresses(entity)
                }
            }

            val groups = mutableListOf<GroupUiModel>()

            groupsList.awaitAll().onEach {
                val membersList = it.members.map {
                    async {
                        getMember(it, isMine)
                    }
                }

                groups.add(
                    it.copy(
                        members = membersList.awaitAll()
                    )
                )
            }

            _groupsFlow.value = groups.also {
                networkListener.onLoading(false)
            }
        }
    }

    override fun getGroups(parent: String, onSuccess: (List<GroupEntity>) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.getGroupsForParent(parent)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun leaveGroup(group: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.leaveGroup(group)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun deleteGroup(group: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.deleteGroup(group)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun switchAdmin(group: String, member: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.switchAdmin(group, member)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun kickUser(group: String, child: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.kickUser(group, child)
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun updateGroupLocation(location: LatLng, group: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.updateGroupLocation(
                    group,
                    location.latitude,
                    location.longitude
                )
            }
            onSuccess{ onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun createGroup(
        child: String,
        name: String,
        description: String,
        onSuccess: (GroupEntity) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.createGroup(child, name, description)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun updateGroup(
        group: String,
        name: String,
        description: String,
        ages: String,
        avatar: String,
        schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
        onSuccess: () -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                groupsRepository.updateGroup(
                    group,
                    UpdateGroupEntity(
                        name = name,
                        desc = description,
                        ages = ages,
                        schedule = schedule,
                        avatar = avatar
                    )
                )
            }
            onSuccess { onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getGroupLocation(address: String, onSuccess:(LatLng?) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                locationRepository.getLocationFromAddress(address)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getGroupAddress(
        latLng: LatLng,
        onSuccess: (String) -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                locationRepository.getAddressFromLocation(latLng)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun uploadGroupAvatar(image: Bitmap, onSuccess: (String) -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                filesRepository.saveAvatar(image)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    private suspend fun getAvatarsAndAddresses(entity: GroupEntity): GroupUiModel {
        networkListener.onLoading(true)

        val avatar = filesRepository.getAvatar(entity.avatar)
        val location =
            locationRepository.getAddressFromLocation(
                latLng = LatLng(
                    entity.location.coordinates[1],
                    entity.location.coordinates[0]
                )
            )

        val members = entity.members
            .groupBy { it.parentId }
            .map { (parentId, children) ->
                MemberUiModel(id = parentId, children = children.map { it.childId }.toList())
            }

        return GroupUiModel(
            id = entity.id,
            adminId = entity.adminId,
            name = entity.name,
            description = entity.description,
            ages = entity.ages,
            schedule = entity.schedule,
            location = LatLng(
                entity.location.coordinates[1],
                entity.location.coordinates[0]
            ),
            avatar = getResult(avatar) ?: Uri.EMPTY,
            address = getResult(location).orEmpty(),
            members = members
        )
    }

    private suspend fun getMember(user: MemberUiModel, isMine: Boolean): MemberUiModel {
        networkListener.onLoading(true)

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

    private fun <T> getResult(result: RequestResult<T>): T? {
        return when(result) {
            is RequestResult.Error -> {
                networkListener.onError(result.error)
                null
            }

            is RequestResult.Success -> result.result
        }
    }
}