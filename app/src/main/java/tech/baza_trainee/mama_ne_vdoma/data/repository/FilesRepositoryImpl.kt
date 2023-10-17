package tech.baza_trainee.mama_ne_vdoma.data.repository

import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import tech.baza_trainee.mama_ne_vdoma.data.api.FilesApi
import tech.baza_trainee.mama_ne_vdoma.data.utils.asCustomResponse
import tech.baza_trainee.mama_ne_vdoma.data.utils.getMessage
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

class FilesRepositoryImpl(
    private val filesApi: FilesApi,
    private val bitmapHelper: BitmapHelper
): FilesRepository {

    override suspend fun saveAvatar(image: Bitmap): RequestResult<String> {
        val file = bitmapHelper.bitmapToFile(image)
        val fileBody = file.asRequestBody(MIME_IMAGE_PNG.toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(IMAGE, file.name, fileBody)
        val result = filesApi.saveAvatar(filePart)
        return if (result.isSuccessful)
            RequestResult.Success(result.body().orEmpty())
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    override suspend fun getAvatar(url: String): RequestResult<Uri> {
        val result = filesApi.getAvatar(url)
        return if (result.isSuccessful)
            RequestResult.Success(bitmapHelper.bitmapUriFromBody(result.body()))
        else RequestResult.Error(result.errorBody()?.asCustomResponse().getMessage())
    }

    companion object {

        private const val IMAGE = "image"
        private const val MIME_IMAGE_PNG = "image/png"
    }
}