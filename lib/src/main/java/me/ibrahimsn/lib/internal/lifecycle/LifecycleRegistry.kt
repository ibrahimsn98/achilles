package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class LifecycleRegistry internal constructor(
    private val flow: MutableSharedFlow<Lifecycle.State>,
    private val debounceMillis: Long
) : Lifecycle by LifecycleFlow(flow), Registry {

    private val job = SupervisorJob()

    private val registryScope = CoroutineScope(job + Dispatchers.IO)

    private val registryChannel = Channel<Lifecycle.State>()

    constructor(debounceMillis: Long = 0L) : this(
        MutableSharedFlow(),
        debounceMillis
    )

    init {
        registryScope.launch {
            registryChannel
                .consumeAsFlow()
                .debounce(debounceMillis)
                .distinctUntilChanged(Lifecycle.State::isEquivalentTo)
                .collect {
                    if (it is Lifecycle.State.Destroyed) {
                        registryChannel.close()
                        registryScope.cancel()
                    } else {
                        flow.emit(it)
                    }
                }
        }
    }

    override fun process(state: Lifecycle.State) {
        registryScope.launch {
            registryChannel.offer(state)
        }
    }
}
