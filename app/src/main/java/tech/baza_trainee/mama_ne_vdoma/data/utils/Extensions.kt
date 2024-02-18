package tech.baza_trainee.mama_ne_vdoma.data.utils

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestResult

fun ResponseBody.asCustomResponse(): CustomResponse =
    Gson().fromJson(this.string(), CustomResponse::class.java)

fun CustomResponse?.getMessage() = this?.message?.first().orEmpty()

fun <T, R> Response<T>.getRequestResult(mapper: (T) -> R): RequestResult<R> {
    return if (isSuccessful)
        body()?.let {
            RequestResult.Success(mapper(it))
        } ?: RequestResult.Error(message().orEmpty())
    else RequestResult.Error(errorBody()?.asCustomResponse().getMessage())
}

fun <T> Response<T>.getRequestResult(): RequestResult<T> {
    return if (isSuccessful) {
        body()?.let {
            RequestResult.Success(it)
        } ?: RequestResult.Error(message().orEmpty())
    } else RequestResult.Error(errorBody()?.asCustomResponse().getMessage())
}