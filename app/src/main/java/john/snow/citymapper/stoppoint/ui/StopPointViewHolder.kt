package john.snow.citymapper.stoppoint.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import john.snow.citymapper.arrivals.model.Arrival
import john.snow.citymapper.arrivals.ui.ArrivalLineCustomView
import john.snow.citymapper.arrivals.viewmodel.ArrivalsViewModel
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.stoppoint.model.StopPoint
import kotlinx.android.synthetic.main.cell_stop_point.view.*

class StopPointViewHolder(
        itemView: View,
        private val owner: LifecycleOwner,
        private val viewModel: ArrivalsViewModel,
        private val arrivalItemClickListener: StopPointNearbyAdapter.ArrivalItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    private var stopPoint: StopPoint? = null

    fun bind(stopPoint: StopPoint) {
        this.stopPoint = stopPoint

        itemView.apply {
            stopPointName.text = stopPoint.commonName
            nextTrain1.reset()
            nextTrain2.reset()
            nextTrain3.reset()
        }
        viewModel.getObservableData(stopPoint.naptanId).observe(owner, Observer<Resource<List<Arrival>>> { response ->
            response?.let { processArrivals(it) }
        })
        viewModel.loadArrivals(stopPoint.naptanId)
    }

    private fun processArrivals(response: Resource<List<Arrival>>) {
        when (response.status) {
            Status.LOADING -> {
                itemView.loadingNextTrains.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                itemView.loadingNextTrains.visibility = View.INVISIBLE
                val times: List<Arrival>? = response.data
                times?.getOrNull(0)?.let { showArrival(itemView.nextTrain1, it) }
                times?.getOrNull(1)?.let { showArrival(itemView.nextTrain2, it) }
                times?.getOrNull(2)?.let { showArrival(itemView.nextTrain3, it) }
            }
            Status.ERROR -> {
                itemView.loadingNextTrains.visibility = View.INVISIBLE
                Log.d("StopPointViewHolder", "error", response.error)
            }
        }
    }

    private fun showArrival(arrivalView: ArrivalLineCustomView, arrival: Arrival) {
        arrivalView.setArrival(arrival)
        arrivalView.setOnClickListener {
            stopPoint?.let { currentStopPoint -> arrivalItemClickListener.onArrivalClicked(currentStopPoint, arrival) }
        }
    }

    fun onViewDetachedFromWindow() {
        stopPoint?.let { viewModel.onDetachedLine(it.naptanId) }
    }
}