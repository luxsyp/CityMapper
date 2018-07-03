package john.snow.citymapper.arrivals.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import john.snow.citymapper.arrivals.model.Arrival
import john.snow.citymapper.arrivals.repository.ArrivalsRepository
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.core.execution.SchedulersFacade
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
class ArrivalsViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var schedulersFacade: SchedulersFacade

    @Mock
    private lateinit var arrivalsRepository: ArrivalsRepository

    private lateinit var viewModel: ArrivalsViewModel

    @Before
    fun setUp() {
        given(schedulersFacade.ui()).willReturn(Schedulers.trampoline())
        given(schedulersFacade.io()).willReturn(Schedulers.trampoline())
        viewModel = ArrivalsViewModel(schedulersFacade, arrivalsRepository)
    }

    @Test
    fun testLoadArrivals() {
        val response = Single.just(listOf(
                Arrival("id_1", "naptanId_1", "lineId_1", "lineName_1", 1),
                Arrival("id_2", "naptanId_2", "lineId_2", "lineName_2", 2),
                Arrival("id_3", "naptanId_3", "lineId_3", "lineName_3", 3)
        ))
        given(arrivalsRepository.getArrivals("naptanId")).willReturn(response)

        viewModel.loadArrivals("naptanId")

        assertEquals(viewModel.getObservableData("naptanId").value?.status, Status.SUCCESS)
        assertEquals(viewModel.getObservableData("naptanId").value?.data, listOf(
                Arrival("id_1", "naptanId_1", "lineId_1", "lineName_1", 1),
                Arrival("id_2", "naptanId_2", "lineId_2", "lineName_2", 2),
                Arrival("id_3", "naptanId_3", "lineId_3", "lineName_3", 3)
        ))
    }

  @Test
    fun testLoadArrivals_SortByTimeToStation() {
      val response = Single.just(listOf(
              Arrival("id_1", "naptanId_1", "lineId_1", "lineName_1", 99999),
              Arrival("id_2", "naptanId_2", "lineId_2", "lineName_2", 100),
              Arrival("id_3", "naptanId_3", "lineId_3", "lineName_3", 300),
              Arrival("id_4", "naptanId_4", "lineId_4", "lineName_4", 200),
              Arrival("id_5", "naptanId_5", "lineId_5", "lineName_5", 99999)
      ))
      given(arrivalsRepository.getArrivals("naptanId")).willReturn(response)

      viewModel.loadArrivals("naptanId")

      assertEquals(viewModel.getObservableData("naptanId").value?.status, Status.SUCCESS)
      assertEquals(viewModel.getObservableData("naptanId").value?.data, listOf(
              Arrival("id_2", "naptanId_2", "lineId_2", "lineName_2", 100),
              Arrival("id_4", "naptanId_4", "lineId_4", "lineName_4", 200),
              Arrival("id_3", "naptanId_3", "lineId_3", "lineName_3", 300)
      ))
    }

    @Test
    fun testLoadArrivals_WhenRepositoryReturnError() {
        val response = Single.error<List<Arrival>>(mock(Throwable::class.java))
        given(arrivalsRepository.getArrivals("naptanId")).willReturn(response)

        viewModel.loadArrivals("naptanId")

        assertEquals(viewModel.getObservableData("naptanId").value?.status, Status.ERROR)
    }
}