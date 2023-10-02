package tech.baza_trainee.mama_ne_vdoma.data.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import tech.baza_trainee.mama_ne_vdoma.data.utils.createEmptyResponse
import java.io.IOException

class AuthInterceptor() : Interceptor {
    companion object {
        const val AUTH_HEADER = "Authorization"
        //        const val EMPTY_TOKEN = "V0VCOg=="//WEB: in base64
        const val EMPTY_TOKEN = "TU9CSUxFOg=="//MOBILE: in base64
        var TOKEN_TYPE: String = "Bearer"
        var LIMPOPO = ""
        var AUTH_TOKEN = EMPTY_TOKEN

        private const val BEARER_TEMPLATE = "Bearer %s"

        fun bearerHeader(token: String) = BEARER_TEMPLATE.format(token)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = try {
        runBlocking {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header(AUTH_HEADER, bearerHeader(AUTH_TOKEN))
            val response = chain.proceed(requestBuilder.build())
//            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {//token expired
//                Timber.d("intercepted 401 call")
//                when (val refresh =
//                    refreshToken(
//                        loginCache,
//                        customerUserId,
//                        refreshToken,
//                        bearerHeader(token),
//                        FingerprintHash.getHashKey()
//                    )
//                ) {
//                    is RequestResult.Success -> {
//                        token = refresh.result.accessToken
//                        refreshToken = refresh.result.refreshToken
//                        Timber.d("success, adding header $token")
//                        requestBuilder.header(AUTH_HEADER, bearerHeader(token))
//                        response.close()
//                        return@runBlocking chain.proceed(requestBuilder.build())
//                    }
//                    is RequestResult.Error -> {
//                        Timber.w("refreshToken failed: ${refresh.error}")
//                        //do nothing or return custom response
//                    }
//                }
//            }
            return@runBlocking response
        }
    } catch (e: IllegalStateException) {
        chain.request().createEmptyResponse(e.message.orEmpty())
    }

//    private suspend fun refreshToken(
//        username: String,
//        customerUserId: Long,
//        refreshToken: String,
//        auth: String = "",
//        fingerprintHash: String
//    ) = try {
//        RequestResult.Success(
//            openApi.refreshToken(
//                username, customerUserId, refreshToken, auth, fingerprintHash
//            )
//        )
//    } catch (e: Exception) {
//        e.handleException()
//    }
}