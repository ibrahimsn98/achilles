package me.ibrahimsn.achilles

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.internal.lifecycle.ApplicationResumedLifecycle
import okhttp3.Dispatcher

class DefaultApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val l = ApplicationResumedLifecycle(this)

        GlobalScope.launch {
            l.collect {
                Log.d("###", "sfds: $it")
            }
        }
    }
}
