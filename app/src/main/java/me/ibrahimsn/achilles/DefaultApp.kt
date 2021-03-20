package me.ibrahimsn.achilles

import android.app.Application
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import me.ibrahimsn.lib.internal.lifecycle.ApplicationResumedLifecycle
import me.ibrahimsn.lib.internal.lifecycle.ConnectivityLifecycle
import okhttp3.Dispatcher

class DefaultApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val l = ApplicationResumedLifecycle(this)
        val s = ConnectivityLifecycle(this)

        GlobalScope.launch {
            val lifeCycles =

            s.combineWith(l).collect {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DefaultApp, "connectivity: $it", Toast.LENGTH_SHORT).show()
                }
                Log.d("###", "lifecycle: $it")
            }
        }
    }
}
