package me.ibrahimsn.lib.internal.state.observer

import kotlinx.coroutines.flow.Flow
import me.ibrahimsn.lib.internal.state.State

interface StateObserver<S : State> {

    fun observe(stateFlow: Flow<S>)
}
