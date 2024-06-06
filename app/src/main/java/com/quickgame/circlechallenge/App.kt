package com.quickgame.circlechallenge

import android.app.Application
import com.quickgame.circlechallenge.di.dataSourceModule
import com.quickgame.circlechallenge.di.dispatcherModule
import com.quickgame.circlechallenge.di.mediaPlayerModule
import com.quickgame.circlechallenge.di.repositoryModule
import com.quickgame.circlechallenge.di.useCaseModule
import com.quickgame.circlechallenge.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                dispatcherModule,
                dataSourceModule,
                useCaseModule,
                repositoryModule,
                mediaPlayerModule,
            )
        }
    }
}