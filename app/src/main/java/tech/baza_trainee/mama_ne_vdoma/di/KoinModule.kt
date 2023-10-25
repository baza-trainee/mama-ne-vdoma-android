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
import tech.baza_trainee.mama_ne_vdoma.data.api.FilesApi
import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.data.datasource.impl.LocationDataSourceImpl
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AuthInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.repository.AuthRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.FilesRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.GroupsRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.LocationRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.UserProfileRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserProfileRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.GroupsInteractorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.LocationInteractorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractor
import tech.baza_trainee.mama_ne_vdoma.presentation.interactors.UserProfileInteractorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigatorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigatorImpl
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GroupSearchStandaloneCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SearchResultsCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main.MainViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request.SearchRequestViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupsStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.UserImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.UserProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import java.util.concurrent.TimeUnit

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
    single<FilesApi> { createAuthorizedApi(get(), get()) }
    single<GroupsApi> { createAuthorizedApi(get(), get()) }
    single<AuthApi> { createCustomApi(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get()) }
    factory<FilesRepository> { FilesRepositoryImpl(get(), get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(androidApplication()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    factory<GroupsRepository> { GroupsRepositoryImpl(get()) }

    factory<UserProfileInteractor> { UserProfileInteractorImpl(get(), get(), get(), get(), get()) }
    factory<LocationInteractor> { LocationInteractorImpl(get(), get(), get()) }
    factory<GroupsInteractor> { GroupsInteractorImpl(get(), get(), get()) }

    single<ScreenNavigator> { ScreenNavigatorImpl() }
    single { BitmapHelper(androidApplication()) }
    single { UserPreferencesDatastoreManager(androidContext()) }
}

val commonScreensModule = module {
    viewModel { (nextRoute: () -> Unit, backRoute: () -> Unit) -> ChildInfoViewModel(nextRoute, backRoute, get(), get()) }
    viewModel { (nextRoute: () -> Unit, backRoute: () -> Unit) -> ChildScheduleViewModel(nextRoute, backRoute, get(), get()) }
}

val userCreateModule = module {
    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { UserProfileCommunicator() }
    viewModel { UserInfoViewModel(get(), get(), get(), get()) }
    viewModel { UserImageCropViewModel(get(), get(), get()) }
    viewModel { UserLocationViewModel(get(), get(), get()) }
    viewModel { ChildrenInfoViewModel(get(), get()) }
    viewModel { ParentScheduleViewModel(get(), get(), get(), get()) }
    viewModel { FullInfoViewModel(get(), get(), get(), get(), get()) }
    viewModel { UserCreateViewModel(get(), get()) }
}

val verifyEmailModule = module {
    viewModel { (email: String, password: String) -> VerifyEmailViewModel(email, password, get(), get()) }
}

val loginKoinModule = module {
    viewModel {
        LoginScreenViewModel(get(), get(), get())
    }
    viewModel { (email: String, otp: String) ->
        NewPasswordScreenViewModel(email, otp, get(), get())
    }
    viewModel { RestorePasswordScreenViewModel(get(), get()) }
}

val standaloneGroupSearchModule = module {
    single { GroupSearchStandaloneCommunicator() }
    viewModel { ChooseChildStandaloneViewModel(get(), get(), get(), get(), get()) }
    viewModel { SetAreaViewModel(get(), get(), get()) }
    viewModel { FoundGroupsStandaloneViewModel(get(), get(), get(), get(), get()) }
}

val mainModule = module {
    single { CropImageCommunicator() }
    single { SearchResultsCommunicator() }
    single { EditProfileCommunicator() }
    single<PageNavigator> { PageNavigatorImpl() }

    viewModel { (page: Int) ->
        HostViewModel(
            page,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { MainViewModel(get(), get()) }
    viewModel { NotificationsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ChooseChildViewModel(get(), get()) }
    viewModel {
        MyGroupsViewModel(get(), get(), get(), get())
    }
    viewModel { (childId: String) ->
        CreateGroupViewModel(childId, get(), get(), get(), get(), get(), get(), get())
    }
    viewModel { (navigator: ScreenNavigator) -> ImageCropViewModel(navigator, get(), get()) }
    viewModel { SearchRequestViewModel(get(), get(), get(), get()) }
    viewModel { SearchResultsViewModel(get(), get(), get(), get()) }
    viewModel { ProfileSettingsViewModel(get(), get(), get(), get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { VerifyNewEmailViewModel(get(), get(), get()) }
}

const val BASE_URL = "https://mommy-not-home.online/back/"

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
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

private const val CHUCKER_CONTENT_MAX_LENGTH = 250000L
private const val TIMEOUT = 30L