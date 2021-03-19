package me.ibrahimsn.lib.internal.state.observer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.ibrahimsn.lib.internal.state.State
import kotlin.coroutines.CoroutineContext

class FlowStateObserver<S : State>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val onStateChanged: suspend (S) -> Unit
) : StateObserver<S> {

    override fun observe(stateFlow: Flow<S>) {
        stateFlow
            .onEach(onStateChanged)
            .flowOn(dispatcher)
            .launchIn(scope)
    }
}
