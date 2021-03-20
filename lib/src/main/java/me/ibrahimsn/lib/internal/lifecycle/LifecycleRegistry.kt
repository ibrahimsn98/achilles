package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class LifecycleRegistry internal constructor(
    private val inChannel: Channel<Lifecycle.State>,
    private val outFlow: MutableStateFlow<Lifecycle.State>,
    private val debounceMillis: Long
) : Lifecycle by LifecycleFlow(outFlow), Registry {

    private val job = SupervisorJob()

    private val registryScope = CoroutineScope(job + Dispatchers.IO)

    constructor(debounceMillis: Long = 0L) : this(
        Channel<Lifecycle.State>(Channel.CONFLATED),
        MutableStateFlow(Lifecycle.State.Started),
        debounceMillis
    )

    init {
        registryScope.launch {
            inChannel
                .consumeAsFlow()
                .debounce(debounceMillis)
                .distinctUntilChanged(Lifecycle.State::isEquivalentTo)
                .collect {
                    if (it is Lifecycle.State.Destroyed) {
                        inChannel.close()
                        registryScope.cancel()
                    } else {
                        outFlow.emit(it)
                    }
                }
        }
    }

    override fun process(state: Lifecycle.State) {
        registryScope.launch {
            inChannel.offer(state)
        }
    }
}
