package br.me.vitorcsouza.train

import android.app.Application
import br.me.vitorcsouza.train.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@TrainApplication)
            modules(appModule)
        }
    }
}
