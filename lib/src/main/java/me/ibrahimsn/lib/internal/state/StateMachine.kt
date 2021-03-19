package me.ibrahimsn.lib.internal.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.internal.state.observer.StateObserver
import kotlin.coroutines.CoroutineContext

class StateMachine<E : Event, S : State> (
    private val initialState: S,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext = Dispatchers.Default,
    private val reducer: (event: E, state: S) -> Unit
) {

    private val stateFlow by lazy {
        MutableStateFlow(initialState)
    }

    val currentState: S
        get() = stateFlow.value

    fun transitionTo(state: S) {
        stateFlow.value = state
    }

    infix fun observeState(observer: StateObserver<S>) =
        observer.observe(stateFlow)

    infix fun emit(event: E) {
        scope.launch(dispatcher) {
            reducer(event, currentState)
        }
    }
}
