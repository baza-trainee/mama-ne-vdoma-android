package tech.baza_trainee.mama_ne_vdoma.presentation.interactors

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.execute
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.networkExecutor
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onLoading
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.onSuccess

interface UserInfoInteractor {

    fun setCoroutineScope(coroutineScope: CoroutineScope)

    fun setNetworkListener(listener: NetworkEventsListener)

    fun validateName(name: String, onSuccess: (ValidField) -> Unit)

    fun validatePhone(phone: String, country: String, onSuccess: (ValidField) -> Unit)

    fun saveUserAvatar(avatar: Bitmap, onSuccess: (Bitmap, Uri) -> Unit, onError: () -> Unit)

    fun uploadUserAvatar(avatar: Bitmap, onSuccess: (String) -> Unit)

    fun deleteUserAvatar(onSuccess: () -> Unit)

    fun getUsetAvatar(avatarId: String, onSuccess: (Uri) -> Unit)

    fun deleteUser(onSuccess: () -> Unit)

    fun saveUserInfo(
        userName: String,
        phoneNumber: String,
        countryCode: String,
        avatarId: String?,
        schedule: ScheduleModel,
        onSuccess: () -> Unit
    )
}

class UserInfoInteractorImpl(
    private val userProfileRepository: UserProfileRepository,
    private val filesRepository: FilesRepository,
    private val phoneNumberUtil: PhoneNumberUtil,
    private val bitmapHelper: BitmapHelper,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): UserInfoInteractor {

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var networkListener: NetworkEventsListener


    override fun setCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun setNetworkListener(listener: NetworkEventsListener) {
        this.networkListener = listener
    }

    override fun deleteUser(onSuccess: () -> Unit) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.deleteUser()
            }
            onSuccess {
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun getUsetAvatar(avatarId: String, onSuccess: (Uri) -> Unit) {
        coroutineScope.networkExecutor {
            execute { filesRepository.getAvatar(avatarId) }
            onSuccess { uri ->
                preferencesDatastoreManager.avatar = uri.toString()
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
                preferencesDatastoreManager.avatar = Uri.EMPTY.toString()
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun saveUserAvatar(avatar: Bitmap, onSuccess: (Bitmap, Uri) -> Unit, onError: () -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val newImage = if (avatar.height > IMAGE_DIM)
                Bitmap.createScaledBitmap(avatar,
                    IMAGE_DIM,
                    IMAGE_DIM, true)
            else avatar
            val newImageSize = bitmapHelper.getSize(newImage)
            if (newImageSize < IMAGE_SIZE) {
                val uri = bitmapHelper.bitmapToFile(newImage).toUri()
                onSuccess(newImage, uri)
            } else onError()
        }
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
        val nameValid = if (name.length in NAME_LENGTH &&
            name.all { it.isLetter() || it.isDigit() || it == ' ' || it == '-' })
            ValidField.VALID
        else
            ValidField.INVALID

        preferencesDatastoreManager.name = name
        onSuccess(nameValid)
    }

    override fun uploadUserAvatar(avatar: Bitmap, onSuccess: (String) -> Unit) {
        coroutineScope.networkExecutor<String> {
            execute {
                filesRepository.saveAvatar(avatar)
            }
            onSuccess {
                onSuccess(it)
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    override fun saveUserInfo(
        userName: String,
        phoneNumber: String,
        countryCode: String,
        avatarId: String?,
        schedule: ScheduleModel,
        onSuccess: () -> Unit
    ) {
        coroutineScope.networkExecutor {
            execute {
                userProfileRepository.saveUserInfo(
                    UserInfoEntity(
                        name = userName,
                        phone = phoneNumber,
                        countryCode = countryCode,
                        avatar = avatarId,
                        schedule = schedule
                    )
                )
            }
            onSuccess {
                preferencesDatastoreManager.apply {
                    name = userName
                    code = countryCode
                    phone = phoneNumber
                }
                onSuccess()
            }
            onError(networkListener::onError)
            onLoading(networkListener::onLoading)
        }
    }

    companion object {

        private val NAME_LENGTH = 2..18
        private const val IMAGE_SIZE = 1 * 1024 * 1024
        private const val IMAGE_DIM = 512
    }
}