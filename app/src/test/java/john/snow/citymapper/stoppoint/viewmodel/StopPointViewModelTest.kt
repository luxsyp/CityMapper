package john.snow.citymapper.stoppoint.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.stoppoint.model.StopPoint
import john.snow.citymapper.stoppoint.repository.StopPointRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
@Suppress("unused")
class StopPointViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var schedulersFacade: SchedulersFacade

    @Mock
    private lateinit var stopPointRepository: StopPointRepository

    private lateinit var viewModel: StopPointViewModel

    @Before
    fun setUp() {
        given(schedulersFacade.ui()).willReturn(Schedulers.trampoline())
        given(schedulersFacade.io()).willReturn(Schedulers.trampoline())
        viewModel = StopPointViewModel(schedulersFacade, stopPointRepository)
    }

    @Test
    fun testLoadStations() {
        val response = Single.just(listOf(StopPoint("0", "Victoria", "Victoria UnderGround Station", 0.0)))
        given(stopPointRepository.getStopPoint(-0.076, 51.508)).willReturn(response)

        viewModel.loadStopPoint(-0.076, 51.508)

        assertEquals(viewModel.stopPointResponse.value?.status, Status.SUCCESS)
        assertEquals(viewModel.stopPointResponse.value?.data, listOf(StopPoint("0", "Victoria", "Victoria UnderGround Station", 0.0)))
    }

    @Test
    fun testLoadStations_WhenRepositoryReturnError() {
        val response = Single.error<List<StopPoint>>(mock(Throwable::class.java))
        given(stopPointRepository.getStopPoint(-0.076, 51.508)).willReturn(response)

        viewModel.loadStopPoint(-0.076, 51.508)

        assertEquals(viewModel.stopPointResponse.value?.status, Status.ERROR)
    }
}