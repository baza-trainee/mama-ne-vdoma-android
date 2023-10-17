package tech.baza_trainee.mama_ne_vdoma.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

interface FilesRepository {

    suspend fun saveAvatar(image: Bitmap): RequestResult<String>

    suspend fun getAvatar(url: String): RequestResult<Uri>
}