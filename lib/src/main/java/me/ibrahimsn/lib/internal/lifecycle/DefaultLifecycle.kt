package me.ibrahimsn.lib.internal.lifecycle

class DefaultLifecycle(
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()
) : Lifecycle by lifecycleRegistry {

    init {
        lifecycleRegistry.process(Lifecycle.State.Started)
    }
}
