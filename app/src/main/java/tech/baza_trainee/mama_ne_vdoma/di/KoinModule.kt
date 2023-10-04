package tech.baza_trainee.mama_ne_vdoma.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.baza_trainee.mama_ne_vdoma.BuildConfig
import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.data.datasource.impl.LocationDataSourceImpl
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.repository.AuthRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.LocationRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.UserProfileRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

val userKoinModule = module {
    single {
        ChuckerInterceptor.Builder(
            androidContext()
        )
            .collector(ChuckerCollector(androidContext()))
            .maxContentLength(CHUCKER_CONTENT_MAX_LENGTH)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
    factory { Gson() }
    single {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }
    single { createOkHttpClient(get(), get()) }
    single { createUserProfileApi(get(), get()) }
    single { createCustomApi<AuthApi>(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(androidApplication()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { BitmapHelper(androidContext()) }
    single { UserProfileCommunicator() }
    viewModel { UserInfoViewModel(get(), get(), get(), get(), get()) }
    viewModel { UserLocationViewModel(get(), get(), get()) }
    viewModel { ChildInfoViewModel(get()) }
    viewModel { ChildScheduleViewModel(get()) }
    viewModel { ChildrenInfoViewModel(get()) }
    viewModel { ParentScheduleViewModel(get()) }
    viewModel { FullInfoViewModel(get()) }
    viewModel { UserCreateViewModel(get()) }
}

val loginKoinModule = module {
    viewModel { LoginScreenViewModel(get()) }
    viewModel { NewPasswordScreenViewModel() }
    viewModel { RestorePasswordScreenViewModel() }
}

const val BASE_URL = "https://tough-moth-trunks.cyclic.cloud/"

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient
): T = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(createGson()))
    .build()
    .create(T::class.java)

fun createGson(): Gson = GsonBuilder()
    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
    .setLenient()
    .create()

private fun createUserProfileApi(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    loggingInterceptor: ChuckerInterceptor
): UserProfileApi {
    val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(loggingInterceptor)

    return createWebService(okHttpBuilder.build())
}

inline fun <reified T> createCustomApi(
    okHttpClient: OkHttpClient
): T {
    return createWebService(okHttpClient)
}

fun createOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    loggingInterceptor: ChuckerInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
//        .hostnameVerifier { _, _ -> true }
//        .addInterceptor(TokenInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

private const val CHUCKER_CONTENT_MAX_LENGTH = 250000L