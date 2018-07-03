package john.snow.citymapper.core.repository

import john.snow.citymapper.arrivals.api.ArrivalsService
import john.snow.citymapper.arrivals.repository.ArrivalsRepository
import john.snow.citymapper.arrivals.repository.ArrivalsRepositoryImpl
import john.snow.citymapper.details.api.LineSequenceService
import john.snow.citymapper.details.repository.LineSequenceRepository
import john.snow.citymapper.details.repository.LineSequenceRepositoryImpl
import john.snow.citymapper.stoppoint.api.StopPointService
import john.snow.citymapper.stoppoint.repository.StopPointRepository
import john.snow.citymapper.stoppoint.repository.StopPointRepositoryImpl
import john.snow.dependency.RepositoryFactory
import john.snow.dependency.ServiceFactory
import kotlin.reflect.KClass

class RepositoryFactoryImpl(
        private val serviceFactory: ServiceFactory
) : RepositoryFactory {

    private val map: Map<KClass<out Any>, () -> Any> = mapOf(
            StopPointRepository::class to { StopPointRepositoryImpl(serviceFactory.get(StopPointService::class)) },
            ArrivalsRepository::class to { ArrivalsRepositoryImpl(serviceFactory.get(ArrivalsService::class)) },
            LineSequenceRepository::class to { LineSequenceRepositoryImpl(serviceFactory.get(LineSequenceService::class)) }
    )

    @Suppress("UNCHECKED_CAST")
    override fun <S : Any> get(kClass: KClass<S>): S {
        val builder: (() -> Any)? = map[kClass]
        return builder?.let {
            it.invoke() as S
        } ?: throw IllegalArgumentException("can not found builder $kClass")
    }

}