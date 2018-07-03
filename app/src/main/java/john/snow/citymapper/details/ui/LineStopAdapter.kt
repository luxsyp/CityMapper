package john.snow.citymapper.details.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import john.snow.citymapper.R
import john.snow.citymapper.details.model.LineStopEntity
import kotlinx.android.synthetic.main.cell_line_stop.view.*
import android.text.style.UnderlineSpan
import android.text.SpannableString


class LineStopAdapter : RecyclerView.Adapter<LineStopAdapter.LineStopViewHolder>() {

    private var data: MutableList<LineStopEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineStopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_line_stop, parent, false)
        return LineStopViewHolder(view)
    }

    override fun onBindViewHolder(holder: LineStopViewHolder, position: Int) {
        data[position].let { lineStopEntity ->
            holder.bind(lineStopEntity)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setLineStops(lineStopEntities: List<LineStopEntity>) {
        data.addAll(lineStopEntities)
        notifyDataSetChanged()
    }

    class LineStopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(lineStopEntity: LineStopEntity) {
            itemView.apply {
                lineStopName.setTextColor(if (lineStopEntity.selected) Color.BLUE else Color.BLACK)
                lineStopName.text = lineStopEntity.name
                stationNearMeImage.visibility = if (lineStopEntity.highlight) View.VISIBLE else View.INVISIBLE
            }
        }
    }

}
