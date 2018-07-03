package john.snow.citymapper.arrivals.api

import okhttp3.mockwebserver.MockResponse
import john.snow.citymapper.api.RecordingSingleObserver
import john.snow.citymapper.arrivals.model.Arrival
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ArrivalsServiceTest {

    private lateinit var service: ArrivalsService

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
                .create(ArrivalsService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStations() {
        enqueueResponse("json/Arrivals.json")

        val observer: RecordingSingleObserver<List<Arrival>> = observerRule.create()

        service.getArrivals("naptanId").subscribe(observer)

        observer.assertValue(listOf(
                Arrival("2001491699", "940GZZLUPCC", "bakerloo", "Bakerloo", 813),
                Arrival("-901824711", "940GZZLUPCC", "bakerloo", "Bakerloo", 214),
                Arrival("1867942018", "940GZZLUPCC", "bakerloo", "Bakerloo", 1473),
                Arrival("-1576431160", "940GZZLUPCC", "bakerloo", "Bakerloo", 394),
                Arrival("350659156", "940GZZLUPCC", "bakerloo", "Bakerloo", 1233),
                Arrival("-901890247", "940GZZLUPCC", "bakerloo", "Bakerloo", 1293),
                Arrival("-768156306", "940GZZLUPCC", "bakerloo", "Bakerloo", 94),
                Arrival("350855764", "940GZZLUPCC", "bakerloo", "Bakerloo", 274),
                Arrival("1868073090", "940GZZLUPCC", "bakerloo", "Bakerloo", 34),
                Arrival("-767959698", "940GZZLUPCC", "bakerloo", "Bakerloo", 633),
                Arrival("-1576365624", "940GZZLUPCC", "bakerloo", "Bakerloo", 933),
                Arrival("1062356336", "940GZZLUPCC", "piccadilly", "Piccadilly", 514),
                Arrival("-108185014", "940GZZLUPCC", "piccadilly", "Piccadilly", 874),
                Arrival("660973351", "940GZZLUPCC", "piccadilly", "Piccadilly", 1594),
                Arrival("-510354311", "940GZZLUPCC", "piccadilly", "Piccadilly", 1414),
                Arrival("-1707749651", "940GZZLUPCC", "piccadilly", "Piccadilly", 1234),
                Arrival("-510157703", "940GZZLUPCC", "piccadilly", "Piccadilly", 34),
                Arrival("-108053942", "940GZZLUPCC", "piccadilly", "Piccadilly", 1114),
                Arrival("-510223239", "940GZZLUPCC", "piccadilly", "Piccadilly", 1294),
                Arrival("-779822693", "940GZZLUPCC", "piccadilly", "Piccadilly", 1054),
                Arrival("1063208304", "940GZZLUPCC", "piccadilly", "Piccadilly", 1714),
                Arrival("-1707618579", "940GZZLUPCC", "piccadilly", "Piccadilly", 1054),
                Arrival("-108119478", "940GZZLUPCC", "piccadilly", "Piccadilly", 334),
                Arrival("-779626085", "940GZZLUPCC", "piccadilly", "Piccadilly", 154),
                Arrival("660317984", "940GZZLUPCC", "piccadilly", "Piccadilly", 694),
                Arrival("660973344", "940GZZLUPCC", "piccadilly", "Piccadilly", 94),
                Arrival("-510354310", "940GZZLUPCC", "piccadilly", "Piccadilly", 574),
                Arrival("-1707749650", "940GZZLUPCC", "piccadilly", "Piccadilly", 1474),
                Arrival("-779757164", "940GZZLUPCC", "piccadilly", "Piccadilly", 454),
                Arrival("1063011697", "940GZZLUPCC", "piccadilly", "Piccadilly", 1414),
                Arrival("-1707815186", "940GZZLUPCC", "piccadilly", "Piccadilly", 1534),
                Arrival("-779822700", "940GZZLUPCC", "piccadilly", "Piccadilly", 754),
                Arrival("-2110189411", "940GZZLUPCC", "piccadilly", "Piccadilly", 1174)
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