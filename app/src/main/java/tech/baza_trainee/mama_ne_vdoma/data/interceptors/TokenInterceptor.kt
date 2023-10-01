package tech.baza_trainee.mama_ne_vdoma.data.interceptors

import okhttp3.Interceptor
import tech.baza_trainee.mama_ne_vdoma.data.utils.createEmptyResponse
import java.io.IOException

class TokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain) = try {
        val token = "Basic " + AuthInterceptor.EMPTY_TOKEN
        chain.proceed(
            chain.request()
                .newBuilder()
                .header(AuthInterceptor.AUTH_HEADER, token)
                .build()
        )
    } catch (e: IllegalStateException) {
        chain.request().createEmptyResponse(e.message.orEmpty())
    }
}