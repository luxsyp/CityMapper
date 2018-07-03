package john.snow.citymapper.core.api

import john.snow.citymapper.arrivals.api.ArrivalsService
import john.snow.citymapper.details.api.LineSequenceService
import john.snow.citymapper.stoppoint.api.StopPointService
import john.snow.dependency.ServiceFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class RetrofitServiceFactory : ServiceFactory {

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    private val map: Map<KClass<out Any>, () -> Any> = mapOf(
            StopPointService::class to { retrofit.create(StopPointService::class.java) },
            ArrivalsService::class to { retrofit.create(ArrivalsService::class.java) },
            LineSequenceService::class to { retrofit.create(LineSequenceService::class.java) }
    )

    @Suppress("UNCHECKED_CAST")
    override fun <S : Any> get(kClass: KClass<S>): S {
        val builder: (() -> Any)? = map[kClass]
        return builder?.let {
            it.invoke() as S
        } ?: throw IllegalArgumentException("can not found builder $kClass")
    }

    companion object {
        const val ENDPOINT = "https://api.tfl.gov.uk/"
    }
}