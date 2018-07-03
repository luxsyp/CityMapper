package john.snow.citymapper.stoppoint.repository

import io.reactivex.Single
import john.snow.citymapper.stoppoint.api.StopPointService
import john.snow.citymapper.stoppoint.model.StopPoint
import john.snow.citymapper.stoppoint.model.StopPointResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.Mockito


class StopPointRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var stopPointService: StopPointService

    private lateinit var stopPointRepository: StopPointRepository

    @Before
    fun setUp() {
        stopPointRepository = StopPointRepositoryImpl(stopPointService)
    }

    @Test
    fun testGetStations_WhenServiceReturnError() {
        val error = mock(Throwable::class.java)
        Mockito.`when`(stopPointService.nearbyStopPoint(51.0, -0.07)).thenReturn(Single.error(error))

        val response = stopPointRepository.getStopPoint(51.0, -0.07)

        response.test().assertError(error)
    }

    @Test
    fun testGetStations_WhenStationListIsEmpty() {
        val stationsResponse = Single.just(StopPointResponse(emptyList()))
        Mockito.`when`(stopPointService.nearbyStopPoint(51.0, -0.07)).thenReturn(stationsResponse)

        val response = stopPointRepository.getStopPoint(51.0, -0.07)

        assertThat(response.test().values()[0]).isEqualTo(emptyList<StopPoint>())
    }

    @Test
    fun testGetStations() {
        val stationsResponse = Single.just(StopPointResponse(listOf(
                StopPoint("id", "name", "commonName", 0.0)
        )))
        Mockito.`when`(stopPointService.nearbyStopPoint(51.0, -0.07)).thenReturn(stationsResponse)

        val response = stopPointRepository.getStopPoint(51.0, -0.07)

        assertThat(response.test().values()[0]).isEqualTo(listOf(
                StopPoint("id", "name", "commonName", 0.0)
        ))
    }
}