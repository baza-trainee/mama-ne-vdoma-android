package tech.baza_trainee.mama_ne_vdoma.data.repository

import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDataModel
import tech.baza_trainee.mama_ne_vdoma.data.mapper.toDomainModel
import tech.baza_trainee.mama_ne_vdoma.data.model.InitChildDto
import tech.baza_trainee.mama_ne_vdoma.data.model.LocationPatchDto
import tech.baza_trainee.mama_ne_vdoma.data.model.UserSearchRequest
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class UserProfileRepositoryImpl(
    private val userProfileApi: UserProfileApi
): UserProfileRepository {

    override suspend fun getUserInfo(): RequestResult<UserProfileEntity> {
        val result = userProfileApi.getUserInfo()
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: UserProfileEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }

    override suspend fun saveUserInfo(userInfo: UserInfoEntity): RequestResult<Unit> {
        val result = userProfileApi.saveUserInfo(userInfo.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun saveUserLocation(latitude: Double, longitude: Double): RequestResult<Unit> {
        val result = userProfileApi.saveUserLocation(LocationPatchDto(latitude, longitude))
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun deleteUser(): RequestResult<Unit> {
        val result = userProfileApi.deleteUser()
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getUserById(userId: String): RequestResult<UserProfileEntity> {
        val result = userProfileApi.getUserById(userId)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.user?.toDomainModel() ?: UserProfileEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getUserByEmail(email: String): RequestResult<UserProfileEntity> {
        val result = userProfileApi.getUserById(UserSearchRequest(email))
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.user?.toDomainModel() ?: UserProfileEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage(), result.code())
    }

    override suspend fun saveChild(name: String, age: Int, isMale: Boolean): RequestResult<ChildEntity?> {
        val result = userProfileApi.saveChild(InitChildDto(name, age, isMale))
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getChildren(): RequestResult<List<ChildEntity>> {
        val result = userProfileApi.getChildren()
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.map { it.toDomainModel() }?.toList().orEmpty())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getChildById(childId: String): RequestResult<ChildEntity> {
        val result = userProfileApi.getChildById(childId)
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel() ?: ChildEntity())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun patchChildById(childId: String, data: PatchChildEntity): RequestResult<ChildEntity?> {
        val result = userProfileApi.patchChildById(childId, data.toDataModel())
        return if (result.isSuccessful)
            RequestResult.Success(result.body()?.toDomainModel())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun deleteChildById(childId: String): RequestResult<Unit> {
        val result = userProfileApi.deleteChildById(childId)
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun deleteUserAvatar(): RequestResult<Unit> {
        val result = userProfileApi.deleteUserAvatar()
        return if (result.isSuccessful)
            RequestResult.Success(Unit)
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }
}