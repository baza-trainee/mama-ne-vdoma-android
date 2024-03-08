package tech.baza_trainee.mama_ne_vdoma.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import java.io.IOException

class ReceivedCookiesInterceptor(
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        val cookies = originalResponse.headers(COOKIES)
        if (cookies.isNotEmpty()) {
            preferencesDatastoreManager.cookies = mutableSetOf<String>().apply {
                for (header in cookies) {
                    add(header)
                }
            }
        }

        return originalResponse
    }

    companion object {

        private const val COOKIES = "Set-Cookie"
    }
}