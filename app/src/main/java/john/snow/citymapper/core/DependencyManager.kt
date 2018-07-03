package john.snow.citymapper.core

import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.core.execution.SchedulersFacadeImpl
import john.snow.citymapper.core.repository.RepositoryFactoryImpl
import john.snow.citymapper.core.api.RetrofitServiceFactory
import john.snow.citymapper.core.viewmodel.ViewModelFactory
import john.snow.dependency.RepositoryFactory
import john.snow.dependency.ServiceFactory

@Suppress("JoinDeclarationAndAssignment")
class DependencyManager {

    val schedulersFacade: SchedulersFacade
    val serviceFactory: ServiceFactory
    val repositoryFactory: RepositoryFactory
    val viewModelFactory: ViewModelFactory

    init {
        schedulersFacade = SchedulersFacadeImpl()
        serviceFactory = RetrofitServiceFactory()
        repositoryFactory = RepositoryFactoryImpl(serviceFactory)
        viewModelFactory = ViewModelFactory(schedulersFacade, repositoryFactory)
    }
}