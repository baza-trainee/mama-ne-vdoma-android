package tech.baza_trainee.mama_ne_vdoma.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin(context: Context) {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(context)
        fragmentFactory()
        modules(
            listOf(
                repoModule,
                verifyEmailModule,
                userCreateModule,
                loginKoinModule,
                standaloneGroupSearchModule,
                mainModule
            )
        )
    }
}