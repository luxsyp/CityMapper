package john.snow.citymapper.arrivals.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import john.snow.citymapper.R
import john.snow.citymapper.arrivals.model.Arrival
import kotlinx.android.synthetic.main.custom_arrival_line.view.*
import android.util.TypedValue



class ArrivalLineCustomView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.custom_arrival_line, this)
        val darkColor = ContextCompat.getColor(context, R.color.cardview_dark_background)
        setBackgroundColor(darkColor)
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        foreground = getDrawable(context, outValue.resourceId)
        isClickable = true
        isFocusable = true
    }

    fun setArrival(arrival: Arrival) {
        timeTextView.text = resources.getString(R.string.format_next_train_time, arrival.timeToStation / 60)
        lineNameTextView.text = arrival.lineName
    }

    fun reset() {
        timeTextView.text = resources.getString(R.string.default_next_train_time)
        lineNameTextView.text = ""
    }

}