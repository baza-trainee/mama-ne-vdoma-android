package tech.baza_trainee.mama_ne_vdoma.di

import android.app.Application
import org.koin.core.context.stopKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}