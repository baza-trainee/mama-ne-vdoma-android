package tech.baza_trainee.mama_ne_vdoma.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.gms.auth.api.identity.Identity
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
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.data.api.AuthApi
import tech.baza_trainee.mama_ne_vdoma.data.api.FilesApi
import tech.baza_trainee.mama_ne_vdoma.data.api.GroupsApi
import tech.baza_trainee.mama_ne_vdoma.data.api.UserAuthApi
import tech.baza_trainee.mama_ne_vdoma.data.api.UserProfileApi
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.data.datasource.impl.LocationDataSourceImpl
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.AddCookiesInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.interceptors.ReceivedCookiesInterceptor
import tech.baza_trainee.mama_ne_vdoma.data.repository.AuthRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.FilesRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.GroupsRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.LocationRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.UserAuthRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.UserProfileRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.domain.model.getDefaultSchedule
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.repository.AuthRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.FilesRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.GroupsRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.domain.repository.UserAuthRepository
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.MainActivityViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.CropImageCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SearchResultsCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group.UpdateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main.MainViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_request.SearchRequestViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results.SearchResultsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials.EditCredentialsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.common.EditProfileCommunicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group.CreateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupsStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.Communicator
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

val gsoModule = module {
    single { Identity.getSignInClient(androidContext()) }
}

val communicatorsModule = module {
    single(named(SCHEDULE)) { Communicator(getDefaultSchedule()) }
    single(named(STRINGS)) { Communicator("") }
    single { CropImageCommunicator() }
    single { SearchResultsCommunicator() }
    single { EditProfileCommunicator() }
    single(named(UPDATE_GROUP)) { Communicator(GroupUiModel()) }
    single { VerifyEmailCommunicator() }
}

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
    single { createOkHttpClient(androidContext(), get(), get(), get()) }
    single<UserProfileApi> { createAuthorizedApi(get(), get(), get()) }
    single<FilesApi> { createAuthorizedApi(get(), get(), get()) }
    single<GroupsApi> { createAuthorizedApi(get(), get(), get()) }
    single<UserAuthApi> { createAuthorizedApi(get(), get(), get()) }
    single<AuthApi> { createCustomApi(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory<UserAuthRepository> { UserAuthRepositoryImpl(get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get(), get()) }
    factory<FilesRepository> { FilesRepositoryImpl(get(), get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(androidApplication()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    factory<GroupsRepository> { GroupsRepositoryImpl(get()) }

    factory<UserProfileInteractor> { UserProfileInteractorImpl(get(), get(), get(), get(), get(), get()) }
    factory<LocationInteractor> { LocationInteractorImpl(get(), get(), get()) }
    factory<GroupsInteractor> { GroupsInteractorImpl(get(), get(), get(), get()) }

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
    viewModel { UserInfoViewModel(get(named(SCHEDULE)), get(), get(), get(), get()) }
    viewModel { UserLocationViewModel(get(), get(), get()) }
    viewModel { ChildrenInfoViewModel(get(), get()) }
    viewModel { ParentScheduleViewModel(get(named(SCHEDULE)), get(), get(), get()) }
    viewModel { FullInfoViewModel(get(named(SCHEDULE)), get(), get(), get(), get()) }
    viewModel { UserCreateViewModel(get(), get(), get(), get()) }
}

val verifyEmailModule = module {
    viewModel { VerifyEmailViewModel(get(), get(), get(), get()) }
}

val loginKoinModule = module {
    viewModel {
        LoginViewModel(get(), get(), get())
    }
    viewModel { NewPasswordScreenViewModel(get(), get()) }
    viewModel { RestorePasswordScreenViewModel(get(), get(), get()) }
    viewModel { MainActivityViewModel(get()) }
}

val standaloneGroupSearchModule = module {
    viewModel { (navigator: ScreenNavigator) -> ImageCropViewModel(navigator, get(), get()) }
    viewModel { (isForSearch: Boolean) ->
        ChooseChildStandaloneViewModel(
            isForSearch,
            get(named(STRINGS)),
            get(),
            get(),
            get()
        )
    }
    viewModel { SetAreaViewModel(get(), get(), get()) }
    viewModel { FoundGroupsStandaloneViewModel(get(named(STRINGS)), get(), get(), get(), get()) }
    viewModel {
        CreateGroupViewModel(get(named(STRINGS)), get(), get(), get(), get(), get(), get(), get())
    }
}

val mainModule = module {
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
            get(),
            get(),
            get()
        )
    }
    viewModel { MainViewModel(get(), get()) }
    viewModel { NotificationsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MyGroupsViewModel(get(), get(), get(), get(), get(), get(named(UPDATE_GROUP))) }
    viewModel { UpdateGroupViewModel(get(), get(named(UPDATE_GROUP)), get(), get(), get(), get()) }
    viewModel { SearchRequestViewModel(get(), get(), get(), get()) }
    viewModel { SearchResultsViewModel(get(), get(), get(), get()) }
    viewModel { ProfileSettingsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { VerifyNewEmailViewModel(get(), get(), get(), get(), get()) }
    viewModel { EditCredentialsViewModel(get(), get(), get(), get(), get()) }
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient
): T = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
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
    loggingInterceptor: ChuckerInterceptor,
    preferencesDatastoreManager: UserPreferencesDatastoreManager
): T {
    val okHttpBuilder = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(preferencesDatastoreManager))
        .addInterceptor(AddCookiesInterceptor(preferencesDatastoreManager))
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
    context: Context,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    loggingInterceptor: ChuckerInterceptor,
    preferencesDatastoreManager: UserPreferencesDatastoreManager
): OkHttpClient {
    val (factory, manager) = createSSLSocketFactory(context)
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .sslSocketFactory(factory, manager)
        .addInterceptor(ReceivedCookiesInterceptor(preferencesDatastoreManager))
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

fun createSSLSocketFactory(context: Context): Pair<SSLSocketFactory, X509TrustManager> {
    // Create a certificate for the production environment
    var ca: Certificate?
    context.resources.openRawResource(R.raw.certificate).use { inputStreamSSH ->
        ca = CertificateFactory.getInstance("X.509").generateCertificate(inputStreamSSH)
    }
    // Create a KeyStore containing our trusted CAs
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(null, null)
    keyStore.setCertificateEntry("ca", ca)
    // Create a TrustManager that trusts the CAs in our KeyStore
    val tmf = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init(keyStore)
    // Create an SSLContext that uses our TrustManager
    val sslContext = SSLContext.getInstance("TLSv1.2")
    sslContext.init(null, tmf.trustManagers, SecureRandom())
    return Pair(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
}

private const val CHUCKER_CONTENT_MAX_LENGTH = 250000L
private const val TIMEOUT = 30L
private const val STRINGS = "STRINGS"
private const val SCHEDULE = "SCHEDULE"
private const val UPDATE_GROUP = "UPDATE_GROUP"