package john.snow.citymapper.core.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import john.snow.citymapper.arrivals.repository.ArrivalsRepository
import john.snow.citymapper.arrivals.viewmodel.ArrivalsViewModel
import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.details.repository.LineSequenceRepository
import john.snow.citymapper.details.viewmodel.LineSequenceViewModel
import john.snow.citymapper.stoppoint.repository.StopPointRepository
import john.snow.citymapper.stoppoint.viewmodel.StopPointViewModel
import john.snow.dependency.RepositoryFactory

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val schedulersFacade: SchedulersFacade,
        private val repositoryFactory: RepositoryFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(StopPointViewModel::class.java) ->
                return StopPointViewModel(schedulersFacade, repositoryFactory.get(StopPointRepository::class)) as T

            modelClass.isAssignableFrom(ArrivalsViewModel::class.java) ->
                return ArrivalsViewModel(schedulersFacade, repositoryFactory.get(ArrivalsRepository::class)) as T

            modelClass.isAssignableFrom(LineSequenceViewModel::class.java) ->
                return LineSequenceViewModel(schedulersFacade, repositoryFactory.get(LineSequenceRepository::class)) as T

        }
        throw IllegalArgumentException("unexpected class $modelClass")
    }
}
