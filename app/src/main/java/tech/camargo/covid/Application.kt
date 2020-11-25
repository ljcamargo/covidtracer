package tech.camargo.covid

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tech.camargo.covid.network.network
import tech.camargo.covid.utils.Linter
import tech.camargo.covid.viewmodels.ResultViewModel

class Application: Application() {

    private val modules = module {
        factory { ResultViewModel() }
        single { Linter(androidContext()) }
        single { Persistent(androidContext()) }
        single { Constants(androidContext()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(listOf(modules, network))
        }
    }
}