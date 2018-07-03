package john.snow.citymapper.stoppoint.ui

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import john.snow.citymapper.R
import john.snow.citymapper.arrivals.model.Arrival
import john.snow.citymapper.arrivals.viewmodel.ArrivalsViewModel
import john.snow.citymapper.stoppoint.model.StopPoint

class StopPointNearbyAdapter(
        private val fragment: Fragment,
        private val arrivalsViewModel: ArrivalsViewModel,
        private val arrivalItemClickListener: ArrivalItemClickListener
) : RecyclerView.Adapter<StopPointViewHolder>() {

    private var data: MutableList<StopPoint> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopPointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_stop_point, parent, false)
        return StopPointViewHolder(view, fragment, arrivalsViewModel, arrivalItemClickListener)
    }

    override fun onBindViewHolder(holder: StopPointViewHolder, position: Int) {
        data[position].let { stopPoint ->
            holder.bind(stopPoint)
        }
    }

    override fun onViewDetachedFromWindow(holder: StopPointViewHolder) {
        holder.onViewDetachedFromWindow()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setStopPoint(stopPoints: List<StopPoint>) {
        data.addAll(stopPoints)
        notifyDataSetChanged()
    }

    interface ArrivalItemClickListener {
        fun onArrivalClicked(stopPoint: StopPoint, arrival: Arrival)
    }
}
