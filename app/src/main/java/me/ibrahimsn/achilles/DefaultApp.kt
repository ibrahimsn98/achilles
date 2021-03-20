package me.ibrahimsn.achilles

import android.app.Application
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import me.ibrahimsn.lib.internal.lifecycle.ApplicationLifecycle
import me.ibrahimsn.lib.internal.lifecycle.ConnectivityLifecycle
import me.ibrahimsn.lib.internal.lifecycle.DefaultLifecycle

class DefaultApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val a = DefaultLifecycle()
        val l = ApplicationLifecycle(this)
        val s = ConnectivityLifecycle(this)

        GlobalScope.launch {
            l.combineWith(a).combineWith(s).collect {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DefaultApp, "connectivity: $it", Toast.LENGTH_SHORT).show()
                }
                Log.d("###", "lifecycle: $it")
            }
        }
    }
}
