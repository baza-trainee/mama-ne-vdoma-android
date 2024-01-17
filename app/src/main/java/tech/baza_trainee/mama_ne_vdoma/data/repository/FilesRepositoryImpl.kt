package tech.baza_trainee.mama_ne_vdoma.data.repository

import android.graphics.Bitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import tech.baza_trainee.mama_ne_vdoma.data.api.FilesApi
import tech.baza_trainee.mama_ne_vdoma.data.utils.getRequestResult
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

        return filesApi.saveAvatar(filePart).getRequestResult {
            it.orEmpty()
        }
    }

    override suspend fun getAvatar(url: String) =
        filesApi.getAvatar(url).getRequestResult {
            bitmapHelper.bitmapUriFromBody(it)
        }

    companion object {

        private const val IMAGE = "image"
        private const val MIME_IMAGE_PNG = "image/png"
    }
}