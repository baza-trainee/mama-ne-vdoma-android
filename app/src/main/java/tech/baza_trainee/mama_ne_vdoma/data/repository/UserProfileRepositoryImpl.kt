package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.InitChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserSearchRequest
import tech.baza_trainee.mama_ne_vdoma.data.utils.getRequestResult
import tech.baza_trainee.mama_ne_vdoma.domain.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class UserProfileRepositoryImpl(
    private val userProfileApi: UserProfileApi,
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
): UserProfileRepository {

    override suspend fun getUserInfo() = userProfileApi.getUserInfo().getRequestResult() {
        it.toDomainModel()
    }

    override suspend fun saveUserInfo(userInfo: UserInfoEntity): RequestResult<Unit> {
        val _userInfo = if (preferencesDatastoreManager.fcmToken.isNotEmpty())
            userInfo.copy(deviceId = preferencesDatastoreManager.fcmToken)
        else
            userInfo

        return userProfileApi.saveUserInfo(_userInfo.toDataModel()).getRequestResult()
    }

    override suspend fun saveUserLocation(latitude: Double, longitude: Double) =
        userProfileApi.saveUserLocation(LocationPatchDto(latitude, longitude)).getRequestResult()

    override suspend fun deleteUser() = userProfileApi.deleteUser().getRequestResult()

    override suspend fun getUserById(userId: String) =
        userProfileApi.getUserById(userId).getRequestResult {
            it.user.toDomainModel()
        }

    override suspend fun getUserByEmail(email: String) =
        userProfileApi.getUserById(UserSearchRequest(email)).getRequestResult {
            it.user.toDomainModel()
        }

    override suspend fun saveChild(name: String, age: Int, isMale: Boolean) =
        userProfileApi.saveChild(InitChildDto(name, age, isMale)).getRequestResult {
            it.toDomainModel()
        }

    override suspend fun getChildren() = userProfileApi.getChildren().getRequestResult { list ->
        list.map { it.toDomainModel() }.toList()
    }

    override suspend fun getChildById(childId: String) =
        userProfileApi.getChildById(childId).getRequestResult {
            it.toDomainModel()
        }

    override suspend fun patchChildById(childId: String, data: PatchChildEntity) =
        userProfileApi.patchChildById(childId, data.toDataModel()).getRequestResult {
            it.toDomainModel()
        }

    override suspend fun deleteChildById(childId: String) =
        userProfileApi.deleteChildById(childId).getRequestResult()

    override suspend fun deleteUserAvatar() = userProfileApi.deleteUserAvatar().getRequestResult()

    override suspend fun deleteUserNotifications() = userProfileApi.deleteUserNotifications().getRequestResult()

    override suspend fun getUserGrade(userId: String) =
        userProfileApi.getUserGrade(userId).getRequestResult { list ->
            list.map { it.toDomainModel() }
        }

    override suspend fun setUserGrade(
        userId: String,
        value: UserRatingDomainModel
    ) = userProfileApi.setUserGrade(userId, value.toDataModel()).getRequestResult()
}