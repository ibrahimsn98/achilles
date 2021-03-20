package me.ibrahimsn.lib.internal.lifecycle

internal class DefaultLifecycle(
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()
) : Lifecycle by lifecycleRegistry {

    init {
        lifecycleRegistry.process(Lifecycle.State.Started)
    }
}
