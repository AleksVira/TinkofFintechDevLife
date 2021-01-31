package com.aleksbsc.tinkoffintechdevlife

import android.app.Application
import com.aleksbsc.tinkoffintechdevlife.di.appKoinModule
import com.aleksbsc.tinkoffintechdevlife.di.networkKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class DevLifeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DevLifeApp)
            modules(
                listOf(
                    networkKoinModule,
                    appKoinModule
                )
            )
        }
    }
}