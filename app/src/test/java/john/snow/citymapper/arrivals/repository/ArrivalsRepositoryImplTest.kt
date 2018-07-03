package john.snow.citymapper.arrivals.repository

import io.reactivex.Single
import john.snow.citymapper.arrivals.api.ArrivalsService
import john.snow.citymapper.arrivals.model.Arrival
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class ArrivalsRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var arrivalsService: ArrivalsService

    private lateinit var arrivalsRepository: ArrivalsRepository

    @Before
    fun setUp() {
        arrivalsRepository = ArrivalsRepositoryImpl(arrivalsService)
    }

    @Test
    fun testArrival_WhenServiceReturnError() {
        val error = Mockito.mock(Throwable::class.java)
        Mockito.`when`(arrivalsService.getArrivals("naptanId")).thenReturn(Single.error(error))

        val response = arrivalsService.getArrivals("naptanId")

        response.test().assertError(error)
    }

    @Test
    fun testArrival_WhenServiceReturnEmptyList() {
        val arrivals = Single.just(emptyList<Arrival>())
        Mockito.`when`(arrivalsService.getArrivals("naptanId")).thenReturn(arrivals)

        val response = arrivalsService.getArrivals("naptanId")

        Assertions.assertThat(response.test().values()[0]).isEqualTo(emptyList<Arrival>())
    }

    @Test
    fun testArrival() {
        val arrivals = Single.just(listOf(
                Arrival("id", "naptanId", "lineId", "lineName", 44)
        ))
        Mockito.`when`(arrivalsService.getArrivals("naptanId")).thenReturn(arrivals)

        val response = arrivalsService.getArrivals("naptanId")

        Assertions.assertThat(response.test().values()[0]).isEqualTo(listOf(
                Arrival("id", "naptanId", "lineId", "lineName", 44)
        ))
    }
}