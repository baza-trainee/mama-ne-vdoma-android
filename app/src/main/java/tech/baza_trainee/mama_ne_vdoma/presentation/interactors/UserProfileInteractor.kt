package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.net.toUri
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.CoroutineScope
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.validateName
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess
import java.time.DayOfWeek

interface UserProfileInteractor {

    fun setUserProfileCoroutineScope(coroutineScope: CoroutineScope)

    fun setUserProfileNetworkListener(listener: NetworkEventsListener)

    fun validateName(name: String, onSuccess: (ValidField) -> Unit)

    fun validatePhone(phone: String, country: String, onSuccess: (ValidField) -> Unit)

    fun saveUserAvatar(avatar: Bitmap, onSuccess: (Bitmap, Uri) -> Unit, onError: () -> Unit)

    fun uploadUserAvatar(avatar: Bitmap, onSuccess: (String) -> Unit)

    fun deleteUserAvatar(onSuccess: () -> Unit)

    fun getUserAvatar(avatarId: String, onSuccess: (Uri) -> Unit)

    fun deleteUser(onSuccess: () -> Unit)

    fun getChildById(childId: String, onSuccess: (ChildEntity?) -> Unit)

    fun patchChild(childId: String, note: String, schedule: SnapshotStateMap<DayOfWeek, DayPeriod>, onSuccess: () -> Unit)

    fun updateParent(user: UserInfoEntity, onSuccess: () -> Unit)

    fun getUserInfo(onSuccess: (UserProfileEntity) -> Unit)

    fun deleteChild(childId: String, onSuccess: () -> Unit)

    fun getChildren(onSuccess: (List<ChildEntity>) -> Unit)
}

class UserProfileInteractorImpl(
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): UserProfileInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener


    override fun setUserProfileCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun setUserProfileNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun deleteUser(onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.deleteUser()
            }
            onSuccess {
                preferencesDatastoreManager.clearData()
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getUserAvatar(avatarId: String, onSuccess: (Uri) -> Unit) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                onSuccess(uri)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun deleteUserAvatar(onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.deleteUserAvatar()
            }
            onSuccess {
                preferencesDatastoreManager.avatar = ""
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun saveUserAvatar(avatar: Bitmap, onSuccess: (Bitmap, Uri) -> Unit, onError: () -> Unit) {
        bitmapHelper.resizeBitmap(
            scope = coroutineScope,
            image = avatar,
            onSuccess = { bmp ->
                val uri = bitmapHelper.bitmapToFile(bmp).toUri()
                onSuccess(bmp, uri)
            },
            onError = onError
        )
    }

    override fun validatePhone(phone: String, country: String, onSuccess: (ValidField) -> Unit) {
        val phoneValid = try {
            val fullNumber = preferencesDatastoreManager.code + phone
            val phoneNumber = phoneNumberUtil.parse(fullNumber, country)
            if (phoneNumberUtil.isPossibleNumber(phoneNumber)) ValidField.VALID
            else ValidField.INVALID
        } catch (e: Exception) {
            ValidField.INVALID
        }
        preferencesDatastoreManager.phone = phone
        onSuccess(phoneValid)
    }

    override fun validateName(name: String, onSuccess: (ValidField) -> Unit) {
        val nameValid = if (name.validateName(NAME_LENGTH)) ValidField.VALID
        else ValidField.INVALID

        preferencesDatastoreManager.name = name
        onSuccess(nameValid)
    }

    override fun uploadUserAvatar(avatar: Bitmap, onSuccess: (String) -> Unit) {
        coroutineScope.networkExecutor<String> {
            execute {
                filesRepository.saveAvatar(avatar)
            }
            onSuccess {
                preferencesDatastoreManager.avatar = it
                onSuccess(it)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getChildById(childId: String, onSuccess: (ChildEntity?) -> Unit) {
        coroutineScope.networkExecutor<ChildEntity?> {
            execute {
                userProfileRepository.getChildById(childId)
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun patchChild(childId: String, note: String, schedule: SnapshotStateMap<DayOfWeek, DayPeriod>, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.patchChildById(
                    childId,
                    PatchChildEntity(
                        comment = note,
                        schedule = schedule
                    )
                )
            }
            onSuccess {
                preferencesDatastoreManager.isChildrenDataProvided = true
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun updateParent(user: UserInfoEntity, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(user)
            }
            onSuccess {
                preferencesDatastoreManager.apply {
                    name = user.name
                    code = user.countryCode
                    phone = user.phone
                    avatar = user.avatar.orEmpty()
                    isUserProfileFilled = true
                }
                getUserAvatar(preferencesDatastoreManager.avatar) {
                    preferencesDatastoreManager.avatarUri = it
                }
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getUserInfo(onSuccess: (UserProfileEntity) -> Unit) {
        coroutineScope.networkExecutor<UserProfileEntity> {
            execute {
                userAuthRepository.getUserInfo()
            }
            onSuccess {
                preferencesDatastoreManager.email = it.email
                onSuccess(it)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun deleteChild(childId: String, onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.deleteChildById(childId)
            }
            onSuccess { onSuccess() }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getChildren(onSuccess: (List<ChildEntity>) -> Unit) {
        coroutineScope.networkExecutor<List<ChildEntity>> {
            execute {
                userProfileRepository.getChildren()
            }
            onSuccess(onSuccess)
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    companion object {

        private val NAME_LENGTH = 2..18
    }
}