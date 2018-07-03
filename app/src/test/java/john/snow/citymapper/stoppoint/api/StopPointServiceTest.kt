package john.snow.citymapper.stoppoint.api

import john.snow.citymapper.api.RecordingSingleObserver
import john.snow.citymapper.stoppoint.model.StopPoint
import john.snow.citymapper.stoppoint.model.StopPointResponse
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


class StopPointServiceTest {

    private lateinit var service: StopPointService

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
                .create(StopPointService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getStations() {
        enqueueResponse("json/StopPoint.json")

        val observer: RecordingSingleObserver<StopPointResponse> = observerRule.create()
        service.nearbyStopPoint(51.0, -0.07).subscribe(observer)

        observer.assertValue(StopPointResponse(listOf(
                StopPoint("940GZZLUWWL",
                        "940GZZLUWWL",
                        "Walthamstow Central Underground Station",
                        301.81792038889114
                )
        )))
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