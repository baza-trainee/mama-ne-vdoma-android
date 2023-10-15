package tech.baza_trainee.mama_ne_vdoma.domain.repository

import android.graphics.Bitmap
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.InitChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.LocationPatchEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.PatchChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface UserProfileRepository {

    suspend fun getUserInfo(): RequestResult<UserProfileEntity>

    suspend fun saveUserInfo(userInfo: UserInfoEntity): RequestResult<Unit>

    suspend fun saveUserLocation(location: LocationPatchEntity): RequestResult<Unit>

    suspend fun getUserById(userId: String): RequestResult<UserProfileEntity>

    suspend fun getUserByEmail(email: String): RequestResult<UserProfileEntity>

    suspend fun deleteUser(): RequestResult<Unit>

    suspend fun saveChild(request: InitChildEntity): RequestResult<ChildEntity?>

    suspend fun getChildren(): RequestResult<List<ChildEntity>>

    suspend fun getChildById(childId: String): RequestResult<ChildEntity>

    suspend fun patchChildById(childId: String, data: PatchChildEntity): RequestResult<ChildEntity?>

    suspend fun deleteChildById(childId: String): RequestResult<Unit>

    suspend fun saveUserAvatar(image: Bitmap): RequestResult<String>

    suspend fun getUserAvatar(url: String): RequestResult<Bitmap>

    suspend fun deleteUserAvatar(): RequestResult<Unit>
}