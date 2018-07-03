package john.snow.citymapper.details.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.details.model.*
import john.snow.citymapper.details.repository.LineSequenceRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
@Suppress("unused")
class LineSequenceViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var schedulersFacade: SchedulersFacade

    @Mock
    private lateinit var lineSequenceRepository: LineSequenceRepository

    private lateinit var viewModel: LineSequenceViewModel

    @Before
    fun setUp() {
        given(schedulersFacade.ui()).willReturn(Schedulers.trampoline())
        given(schedulersFacade.io()).willReturn(Schedulers.trampoline())
        viewModel = LineSequenceViewModel(schedulersFacade, lineSequenceRepository)
    }

    @Test
    fun testGetLineSequence_WhenRepositoryReturnError() {
        val response = Single.error<LineSequenceResponse>(Mockito.mock(Throwable::class.java))
        given(lineSequenceRepository.getLineSequence("lineId")).willReturn(response)

        viewModel.loadLineSequence("lineId", "naptanId", 0.0, 0.0)

        assertEquals(viewModel.lineSequenceResponse.value?.status, Status.ERROR)
    }

    @Test
    fun testGetLineSequence() {
        val response = LineSequenceResponse(
                "victoria",
                "Victoria",
                listOf(
                        LineStation("940GZZLUGPK", "Green Park Underground Station", -0.142787, 51.506947),
                        LineStation("940GZZLUOXC", "Oxford Circus Underground Station", -0.141903, 51.515224),
                        LineStation("940GZZLUPCO", "Pimlico Underground Station", -0.133761, 51.489097),
                        LineStation("940GZZLUSKW", "Stockwell Underground Station", -0.122644, 51.472184),
                        LineStation("940GZZLUWRR", "Warren Street Underground Station", -0.138321, 51.524951),
                        LineStation("HUBBHO", "Blackhorse Road", -0.041185, 51.586768),
                        LineStation("HUBBRX", "Brixton", -0.114531, 51.462961),
                        LineStation("HUBEUS", "Euston", -0.132754, 51.527365),
                        LineStation("HUBFPK", "Finsbury Park", -0.105876, 51.564778),
                        LineStation("HUBHHY", "Highbury & Islington", -0.103538, 51.546269),
                        LineStation("HUBKGX", "King\'s Cross & St Pancras International", -0.123538, 51.531683),
                        LineStation("HUBSVS", "Seven Sisters", -0.073306, 51.582931),
                        LineStation("HUBTOM", "Tottenham Hale", -0.06024, 51.588315),
                        LineStation("HUBVIC", "Victoria", -0.143826, 51.495812),
                        LineStation("HUBVXH", "Vauxhall", -0.123303, 51.485739),
                        LineStation("HUBWHC", "Walthamstow Central", -0.019842, 51.582948)
                ),
                listOf(LineStopPointSequences(listOf(
                        StopPointOrder("940GZZLUWWL", "HUBWHC"),
                        StopPointOrder("940GZZLUBLR", "HUBBHO"),
                        StopPointOrder("940GZZLUTMH", "HUBTOM"),
                        StopPointOrder("940GZZLUSVS", "HUBSVS"),
                        StopPointOrder("940GZZLUFPK", "HUBFPK"),
                        StopPointOrder("940GZZLUHAI", "HUBHHY"),
                        StopPointOrder("940GZZLUKSX", "HUBKGX"),
                        StopPointOrder("940GZZLUEUS", "HUBEUS"),
                        StopPointOrder("940GZZLUWRR", null),
                        StopPointOrder("940GZZLUOXC", null),
                        StopPointOrder("940GZZLUGPK", null),
                        StopPointOrder("940GZZLUVIC", "HUBVIC"),
                        StopPointOrder("940GZZLUPCO", null),
                        StopPointOrder("940GZZLUVXL", "HUBVXH"),
                        StopPointOrder("940GZZLUSKW", null),
                        StopPointOrder("940GZZLUBXN", "HUBBRX")
                )))
        )

        given(lineSequenceRepository.getLineSequence("lineId")).willReturn(Single.just(response))

        viewModel.loadLineSequence("lineId", "naptanId", -0.126629, 51.513665)

        assertEquals(viewModel.lineSequenceResponse.value?.status, Status.SUCCESS)
        assertEquals(viewModel.lineSequenceResponse.value?.data, listOf(
                LineStopEntity("Walthamstow Central", false, 7499434.650728466),
                LineStopEntity("Blackhorse Road", false, 7500912.202354443),
                LineStopEntity("Tottenham Hale", false, 7502121.642316778),
                LineStopEntity("Seven Sisters", false, 7502571.634927752),
                LineStopEntity("Finsbury Park", false, 7503416.497999577),
                LineStopEntity("Highbury & Islington", false, 7502190.717884766),
                LineStopEntity("King\'s Cross & St Pancras International", false, 7502509.348900321),
                LineStopEntity("Euston", false, 7502797.76556008),
                LineStopEntity("Warren Street Underground Station", false, 7502983.487242779),
                LineStopEntity("Oxford Circus Underground Station", false, 7502623.016166101),
                LineStopEntity("Green Park Underground Station", false, 7502189.025358183),
                LineStopEntity("Victoria", false, 7501596.432358101),
                LineStopEntity("Pimlico Underground Station", false, 7500609.490700291),
                LineStopEntity("Vauxhall", false, 7499796.451069682),
                LineStopEntity("Stockwell Underground Station", false, 7498961.550635371),
                LineStopEntity("Brixton", false, 7497941.938622455)
        ))
    }

}