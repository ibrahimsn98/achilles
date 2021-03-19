package me.ibrahimsn.achilles

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.onEach
import me.ibrahimsn.lib.Achilles
import okhttp3.OkHttpClient

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val achilles = Achilles.Builder()
            .baseUrl("wss://echo.websocket.org")
            .client(OkHttpClient().newBuilder().build())
            .logTraffic(true)
            .encodePayload(true)
            .build()


        val service = achilles.create(SocketService::class.java)

        CoroutineScope(IO).launch {
            service.receiveEcho().onEach {
                Log.d("MainActivity", "Response: $it")
            }

            for (i in 0..10) {
                service.sendEcho("Name", "Surname")
                delay(1000)
            }
        }
    }
}