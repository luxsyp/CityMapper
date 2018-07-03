package john.snow.citymapper.core

import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.core.viewmodel.ViewModelFactory
import john.snow.dependency.Injection
import john.snow.dependency.RepositoryFactory
import john.snow.dependency.ServiceFactory

class InjectionMapper(private val dependencyManager: DependencyManager) {

    fun inject() {
        Injection.set(SchedulersFacade::class, dependencyManager.schedulersFacade)
        Injection.set(ServiceFactory::class, dependencyManager.serviceFactory)
        Injection.set(RepositoryFactory::class, dependencyManager.repositoryFactory)
        Injection.set(ViewModelFactory::class, dependencyManager.viewModelFactory)
    }
}