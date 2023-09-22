package tech.baza_trainee.mama_ne_vdoma.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.baza_trainee.mama_ne_vdoma.data.datasource.LocationDataSource
import tech.baza_trainee.mama_ne_vdoma.data.datasource.impl.LocationDataSourceImpl
import tech.baza_trainee.mama_ne_vdoma.data.repository.LocationRepositoryImpl
import tech.baza_trainee.mama_ne_vdoma.domain.repository.LocationRepository
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel

val userKoinModule = module {
    viewModel { UserCreateViewModel(get()) }
    factory<LocationDataSource> { LocationDataSourceImpl(androidApplication()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
}