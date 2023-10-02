package tech.baza_trainee.mama_ne_vdoma.domain.repository

import tech.baza_trainee.mama_ne_vdoma.domain.model.UserInfoEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserLocationEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.UserProfileEntity
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface UserProfileRepository {

    suspend fun getUserInfo(): RequestResult<UserProfileEntity?>
    suspend fun saveUserInfo(userInfo: UserInfoEntity): RequestResult<Unit>
    suspend fun saveUserLocation(location: UserLocationEntity): RequestResult<Unit>
}