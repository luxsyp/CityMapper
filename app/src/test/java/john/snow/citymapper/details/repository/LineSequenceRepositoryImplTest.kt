package john.snow.citymapper.details.repository

import io.reactivex.Single
import john.snow.citymapper.details.api.LineSequenceService
import john.snow.citymapper.details.model.LineSequenceResponse
import john.snow.citymapper.details.model.LineStation
import john.snow.citymapper.details.model.LineStopPointSequences
import john.snow.citymapper.details.model.StopPointOrder
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class LineSequenceRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var lineSequenceService: LineSequenceService

    private lateinit var lineSequenceRepository: LineSequenceRepository

    @Before
    fun setUp() {
        lineSequenceRepository = LineSequenceRepositoryImpl(lineSequenceService)
    }

    @Test
    fun testGetLineSequence_WhenServiceReturnError() {
        val error = Mockito.mock(Throwable::class.java)
        Mockito.`when`(lineSequenceRepository.getLineSequence("lineId")).thenReturn(Single.error(error))

        val response = lineSequenceRepository.getLineSequence("lineId")

        response.test().assertError(error)
    }

    @Test
    fun testGetLineSequence_WhenStationListIsEmpty() {
        val stationsResponse = Single.just(LineSequenceResponse("lineId", "lineName", emptyList(), emptyList()))
        Mockito.`when`(lineSequenceRepository.getLineSequence("lineId")).thenReturn(stationsResponse)

        val response = lineSequenceRepository.getLineSequence("lineId")

        Assertions.assertThat(response.test().values()[0]).isEqualTo(LineSequenceResponse("lineId", "lineName", emptyList(), emptyList()))
    }

    @Test
    fun testGetLineSequence() {
        val stationsResponse = Single.just(
                LineSequenceResponse("lineId", "lineName",
                        listOf(LineStation("940GZZLUGPK", "Green Park Underground Station", -0.142787, 51.506947)),
                        listOf(LineStopPointSequences(listOf(StopPointOrder("940GZZLUWWL", "HUBWHC")))))
        )
        Mockito.`when`(lineSequenceRepository.getLineSequence("lineId")).thenReturn(stationsResponse)

        val response = lineSequenceRepository.getLineSequence("lineId")

        Assertions.assertThat(response.test().values()[0]).isEqualTo(
                LineSequenceResponse("lineId", "lineName",
                        listOf(LineStation("940GZZLUGPK", "Green Park Underground Station", -0.142787, 51.506947)),
                        listOf(LineStopPointSequences(listOf(StopPointOrder("940GZZLUWWL", "HUBWHC")))))
        )
    }
}