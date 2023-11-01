package tech.baza_trainee.mama_ne_vdoma.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import java.io.IOException


class AddCookiesInterceptor(
    private val preferencesDatastoreManager: UserPreferencesDatastoreManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val preferences = preferencesDatastoreManager.cookies

        // Use the following if you need everything in one line.
        // Some APIs die if you do it differently.
        val cookieString = StringBuilder()
        preferences.forEach {
            val parser = it.split(";")
            cookieString.append(parser[0] + "; ")
        }
        builder.addHeader("Cookie", cookieString.toString())

//        for (cookie in preferences) {
//            builder.addHeader("Cookie", cookie)
//        }

        return chain.proceed(builder.build())
    }
}