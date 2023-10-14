package tech.baza_trainee.mama_ne_vdoma.di

import androidx.lifecycle.SavedStateHandle
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
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.baza_trainee.mama_ne_vdoma.BuildConfig
import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.data.datasource.impl.LocationDataSourceImpl
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.repository.AuthRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.GroupsRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.LocationRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.UserProfileRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigatorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.MainScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

val repoModule = module {
    single {
        ChuckerInterceptor.Builder(androidContext())
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
    single<UserProfileApi> { createAuthorizedApi(get(), get()) }
    single<GroupsApi> { createAuthorizedApi(get(), get()) }
    single<AuthApi> { createCustomApi(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get(), get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(androidApplication()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    factory<GroupsRepository> { GroupsRepositoryImpl(get()) }

    single<ScreenNavigator> { ScreenNavigatorImpl() }
}

val userCreateModule = module {
    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { BitmapHelper(androidApplication()) }
    single { UserProfileCommunicator() }
    viewModel { UserInfoViewModel(get(), get(), get(), get(), get()) }
    viewModel { ImageCropViewModel(get(), get(), get()) }
    viewModel { UserLocationViewModel(get(), get(), get(), get()) }
    viewModel { ChildInfoViewModel(get(), get(), get()) }
    viewModel { ChildScheduleViewModel(get(), get(), get()) }
    viewModel { ChildrenInfoViewModel(get(), get()) }
    viewModel { ParentScheduleViewModel(get(), get(), get(), get()) }
    viewModel { FullInfoViewModel(get(), get(), get(), get()) }
    viewModel { UserCreateViewModel(get(), get()) }
}

val verifyEmailModule = module {
    viewModel { (email: String, password: String) -> VerifyEmailViewModel(email, password, get(), get()) }
}

val loginKoinModule = module {
    viewModel {
        LoginScreenViewModel(get(), get())
    }
    viewModel { (email: String, otp: String) ->
        NewPasswordScreenViewModel(
            email,
            otp,
            get(),
            get()
        )
    }
    viewModel { RestorePasswordScreenViewModel(get(), get()) }
}

val mainModule = module {
    single<ScreenNavigator>(named(SINGLETON_FOR_MAIN)) { ScreenNavigatorImpl() }
    single(named(SINGLETON_FOR_MAIN)) { SavedStateHandle() }
    viewModel { SetAreaViewModel(get(), get(), get()) }
    viewModel { HostScreenViewModel(get(named(SINGLETON_FOR_MAIN)), get(), get(named(SINGLETON_FOR_MAIN))) }
    viewModel { MainScreenViewModel(get(named(SINGLETON_FOR_MAIN)), get(), get(named(SINGLETON_FOR_MAIN))) }
    viewModel { ChooseChildScreenViewModel(get(), get(named(SINGLETON_FOR_MAIN))) }
    viewModel { MyGroupsScreenViewModel(get(named(SINGLETON_FOR_MAIN)), get(), get(), get(), get(named(SINGLETON_FOR_MAIN))) }
    viewModel { (childId: String) -> CreateGroupScreenViewModel(childId, get(named(SINGLETON_FOR_MAIN)), get(), get(), get()) }
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

private inline fun <reified T: Any> createAuthorizedApi(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    loggingInterceptor: ChuckerInterceptor
): T {
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
private const val SINGLETON_FOR_MAIN = "MAIN"