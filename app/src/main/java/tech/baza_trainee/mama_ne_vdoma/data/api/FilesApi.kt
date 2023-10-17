package tech.baza_trainee.mama_ne_vdoma.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FilesApi {

    @POST("api/files/image")
    @Multipart
    suspend fun saveAvatar(@Part image: MultipartBody.Part): Response<String>

    @GET("api/files/{url}")
    suspend fun getAvatar(@Path("url") url: String): Response<ResponseBody>
}