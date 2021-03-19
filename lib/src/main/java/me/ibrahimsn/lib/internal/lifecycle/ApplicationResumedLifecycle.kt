package me.ibrahimsn.lib.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.internal.core.ShutdownReason

class ApplicationResumedLifecycle(
    application: Application,
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()
) : Lifecycle by lifecycleRegistry {

    init {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())
    }

    private inner class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityPaused(activity: Activity) {
            GlobalScope.launch {
                lifecycleRegistry.emit(
                    Lifecycle.State.Stopped.WithReason(
                        ShutdownReason(1000, "App is paused")
                    )
                )
            }

            val s = lifecycleRegistry.tryEmit(
                Lifecycle.State.Stopped.WithReason(
                    ShutdownReason(1000, "App is paused")
                )
            )

            Log.d("###", "sadasdsa1: $s")
        }

        override fun onActivityResumed(activity: Activity) {
            Log.d("###", "sadasdsa2")
            lifecycleRegistry.tryEmit(Lifecycle.State.Started)
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityDestroyed(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    }
}
