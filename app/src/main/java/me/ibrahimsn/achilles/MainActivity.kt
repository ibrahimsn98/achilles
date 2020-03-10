package me.ibrahimsn.achilles

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.achilleslib.Achilles
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val achilles = Achilles.Builder()
            .baseUrl("wss://echo.websocket.org")
            .client(OkHttpClient().newBuilder().build())
            .build()

        val service = achilles.create(SocketService::class.java)

        disposable.add(service.receiveEcho()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               Log.d("MainActivity", "Response: $it")
            }, {
                Log.d("MainActivity", "Error:", it)
            }))

        service.sendEcho("Name", "Surname")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}