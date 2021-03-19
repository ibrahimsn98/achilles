package me.ibrahimsn.lib.internal.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.internal.state.observer.StateObserver
import kotlin.coroutines.CoroutineContext

class StateMachine<A : Action, S : State> (
    private val initialState: S,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext = Dispatchers.Default,
    private val reducer: (action: A, state: S) -> Unit
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

    infix fun emit(action: A) {
        scope.launch(dispatcher) {
            reducer(action, currentState)
        }
    }
}
