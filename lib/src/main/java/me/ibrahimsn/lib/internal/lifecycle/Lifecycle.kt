package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.flow.MutableSharedFlow
import me.ibrahimsn.lib.internal.core.ShutdownReason

interface Lifecycle : MutableSharedFlow<Lifecycle.State> {

    fun combineWith(vararg others: Lifecycle): Lifecycle

    sealed class State {

        object Started : State()

        sealed class Stopped : State() {

            data class WithReason(
                val shutdownReason: ShutdownReason = ShutdownReason.GRACEFUL
            ) : Stopped()

            object AndAborted : Stopped()
        }

        object Destroyed : State()
    }
}
