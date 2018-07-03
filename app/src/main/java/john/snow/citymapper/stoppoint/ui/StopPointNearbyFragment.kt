package john.snow.citymapper.stoppoint.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import john.snow.citymapper.R
import john.snow.citymapper.arrivals.model.Arrival
import john.snow.citymapper.arrivals.viewmodel.ArrivalsViewModel
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.core.viewmodel.ViewModelFactory
import john.snow.citymapper.details.ui.LineDetailActivity
import john.snow.citymapper.location.LondonConstants.LONDON_LATITUDE
import john.snow.citymapper.location.LondonConstants.LONDON_LONGITUDE
import john.snow.citymapper.stoppoint.model.StopPoint
import john.snow.citymapper.stoppoint.viewmodel.StopPointViewModel
import john.snow.dependency.Injection
import kotlinx.android.synthetic.main.fragment_stop_points_nearby.*


class StopPointNearbyFragment : Fragment() {
    private lateinit var stopPointViewModel: StopPointViewModel
    private lateinit var stopPointNearbyAdapter: StopPointNearbyAdapter
    private lateinit var arrivalsViewModel: ArrivalsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val viewModelFactory = Injection.get(ViewModelFactory::class)
        stopPointViewModel = ViewModelProviders.of(this, viewModelFactory).get(StopPointViewModel::class.java)
        arrivalsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArrivalsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_stop_points_nearby, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val longitude = arguments?.getDouble(EXTRA_LONGITUDE) ?: LONDON_LONGITUDE
        val latitude = arguments?.getDouble(EXTRA_LATITUDE) ?: LONDON_LATITUDE

        val arrivalItemClickListener = ArrivalItemClickListenerImpl(longitude, latitude)
        stopPointNearbyAdapter = StopPointNearbyAdapter(this, arrivalsViewModel, arrivalItemClickListener)
        val separatorHeight = resources.getDimension(R.dimen.item_separator_height).toInt()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopPointNearbyAdapter
            addItemDecoration(VerticalSpaceItemDecoration(separatorHeight))
        }
        retryButton.setOnClickListener { stopPointViewModel.loadStopPoint(longitude, latitude) }

        stopPointViewModel.stopPointResponse.observe(this, Observer<Resource<List<StopPoint>>> { response ->
            response?.let { processResponse(it) }
        })
        stopPointViewModel.loadStopPoint(longitude, latitude)
    }

    override fun onStart() {
        super.onStart()
        arrivalsViewModel.restartMonitoring()
    }

    override fun onPause() {
        super.onPause()
        arrivalsViewModel.pauseMonitoring()
    }

    private fun processResponse(response: Resource<List<StopPoint>>) {
        when (response.status) {
            Status.LOADING -> {
                viewFlipper.displayedChild = DISPLAY_LOADING
            }
            Status.SUCCESS -> {
                viewFlipper.displayedChild = DISPLAY_LIST
                response.data?.let { stopPointNearbyAdapter.setStopPoint(it) }
            }
            Status.ERROR -> {
                viewFlipper.displayedChild = DISPLAY_ERROR
            }
        }
    }

    private inner class ArrivalItemClickListenerImpl(
            private val longitude: Double,
            private val latitude: Double
    ) : StopPointNearbyAdapter.ArrivalItemClickListener {
        override fun onArrivalClicked(stopPoint: StopPoint, arrival: Arrival) {
            context?.let { LineDetailActivity.start(it, longitude, latitude, arrival.lineName, arrival.lineId, arrival.naptanId) }
        }
    }

    companion object {
        private const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        private const val EXTRA_LATITUDE = "EXTRA_LATITUDE"

        private const val DISPLAY_LOADING = 0
        private const val DISPLAY_ERROR = 1
        private const val DISPLAY_LIST = 2

        fun newInstance(longitude: Double, latitude: Double): Fragment {
            return StopPointNearbyFragment().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_LONGITUDE, longitude)
                    putDouble(EXTRA_LATITUDE, latitude)
                }
            }
        }
    }

}