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
        if (originalResponse.headers(COOKIES).isNotEmpty()) {
            preferencesDatastoreManager.cookies = mutableSetOf<String>().apply {
                for (header in originalResponse.headers(COOKIES)) {
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