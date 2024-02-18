package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserRatingDomainModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface UserProfileRepository {

    suspend fun saveUserInfo(userInfo: UserInfoEntity): RequestResult<Unit>

    suspend fun saveUserLocation(latitude: Double, longitude: Double): RequestResult<Unit>

    suspend fun getUserById(userId: String): RequestResult<UserProfileEntity>

    suspend fun getUserByEmail(email: String): RequestResult<UserProfileEntity>

    suspend fun deleteUser(): RequestResult<Unit>

    suspend fun saveChild(name: String, age: Int, isMale: Boolean): RequestResult<ChildEntity?>

    suspend fun getChildren(): RequestResult<List<ChildEntity>>

    suspend fun getChildById(childId: String): RequestResult<ChildEntity>

    suspend fun patchChildById(childId: String, data: PatchChildEntity): RequestResult<ChildEntity?>

    suspend fun deleteChildById(childId: String): RequestResult<Unit>

    suspend fun deleteUserAvatar(): RequestResult<Unit>

    suspend fun deleteUserNotifications(): RequestResult<Unit>

    suspend fun getUserGrade(userId: String): RequestResult<List<UserRatingDomainModel>>

    suspend fun setUserGrade(userId: String, value: UserRatingDomainModel): RequestResult<Unit>
}