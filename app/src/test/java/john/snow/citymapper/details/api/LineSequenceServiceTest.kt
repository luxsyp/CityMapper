package john.snow.citymapper.details.api

import john.snow.citymapper.api.RecordingSingleObserver
import john.snow.citymapper.details.model.LineSequenceResponse
import john.snow.citymapper.details.model.LineStation
import john.snow.citymapper.details.model.LineStopPointSequences
import john.snow.citymapper.details.model.StopPointOrder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LineSequenceServiceTest {
    private lateinit var service: LineSequenceService

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    private val observerRule = RecordingSingleObserver.Rule()

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(LineSequenceService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStations() {
        enqueueResponse("json/Sequence.json")

        val observer: RecordingSingleObserver<LineSequenceResponse> = observerRule.create()
        service.getLineSequence("lineId").subscribe(observer)

        observer.assertValue(LineSequenceResponse(
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
        ))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
                .getResourceAsStream(fileName)
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
                mockResponse
                        .setBody(source.readString(Charsets.UTF_8))
        )
    }
}