package john.snow.citymapper.details.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import john.snow.citymapper.R
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.common.Status
import john.snow.citymapper.core.viewmodel.ViewModelFactory
import john.snow.citymapper.details.model.LineStopEntity
import john.snow.citymapper.details.viewmodel.LineSequenceViewModel
import john.snow.citymapper.location.LondonConstants
import john.snow.citymapper.stoppoint.ui.VerticalSpaceItemDecoration
import john.snow.dependency.Injection
import kotlinx.android.synthetic.main.activity_line_details.*

class LineDetailActivity : AppCompatActivity() {
    private lateinit var lineStopAdapter: LineStopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_details)

        // get extra
        val longitude = intent?.extras?.getDouble(EXTRA_LONGITUDE)
                ?: LondonConstants.LONDON_LONGITUDE
        val latitude = intent?.extras?.getDouble(EXTRA_LATITUDE) ?: LondonConstants.LONDON_LATITUDE
        val lineName = intent?.extras?.getString(EXTRA_LINE_NAME, "") ?: "Victoria"
        val lineId = intent?.extras?.getString(EXTRA_LINE_ID, "") ?: "victoria"
        val hubNaptanId = intent?.extras?.getString(EXTRA_STATION_NAPTAN_ID, "") ?: "940GZZLUSKW"

        // vm
        val viewModelFactory = Injection.get(ViewModelFactory::class)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LineSequenceViewModel::class.java)

        // update title
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = lineName
        }
        lineStopAdapter = LineStopAdapter()
        val separatorHeight = resources.getDimension(R.dimen.item_separator_height).toInt()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lineStopAdapter
            addItemDecoration(VerticalSpaceItemDecoration(separatorHeight))
        }
        retryButton.setOnClickListener { viewModel.loadLineSequence(lineId, hubNaptanId, latitude, longitude) }

        viewModel.lineSequenceResponse.observe(this, Observer<Resource<List<LineStopEntity>>> { response ->
            response?.let { processResponse(it) }
        })
        viewModel.loadLineSequence(lineId, hubNaptanId, latitude, longitude)
    }

    private fun processResponse(response: Resource<List<LineStopEntity>>) {
        when (response.status) {
            Status.LOADING -> {
                viewFlipper.displayedChild = DISPLAY_LOADING
            }
            Status.SUCCESS -> {
                viewFlipper.displayedChild = DISPLAY_LIST
                response.data?.let { lineStopAdapter.setLineStops(it) }
            }
            Status.ERROR -> {
                viewFlipper.displayedChild = DISPLAY_ERROR
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                android.R.id.home -> {
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    companion object {
        private const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        private const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        private const val EXTRA_LINE_NAME = "EXTRA_LINE_NAME"
        private const val EXTRA_LINE_ID = "EXTRA_LINE_ID"
        private const val EXTRA_STATION_NAPTAN_ID = "EXTRA_STATION_NAPTAN_ID"

        private const val DISPLAY_LOADING = 0
        private const val DISPLAY_ERROR = 1
        private const val DISPLAY_LIST = 2

        fun start(context: Context,
                  longitude: Double,
                  latitude: Double,
                  lineName: String,
                  lineId: String,
                  hubNaptanId: String
        ) {
            val intent = Intent(context, LineDetailActivity::class.java).apply {
                putExtra(EXTRA_LONGITUDE, longitude)
                putExtra(EXTRA_LATITUDE, latitude)
                putExtra(EXTRA_LINE_NAME, lineName)
                putExtra(EXTRA_LINE_ID, lineId)
                putExtra(EXTRA_STATION_NAPTAN_ID, hubNaptanId)
            }
            context.startActivity(intent)
        }
    }
}