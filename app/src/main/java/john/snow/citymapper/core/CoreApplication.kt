package john.snow.citymapper.core

import android.app.Application

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val dependencyManager = DependencyManager()
        val injectionMapper = InjectionMapper(dependencyManager)
        injectionMapper.inject()
    }
}