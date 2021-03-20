package me.ibrahimsn.lib.internal.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
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
            lifecycleRegistry.process(
                Lifecycle.State.Stopped.WithReason(
                    ShutdownReason(1000, "App is paused")
                )
            )
        }

        override fun onActivityResumed(activity: Activity) {
            lifecycleRegistry.process(Lifecycle.State.Started)
        }

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityDestroyed(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    }
}
