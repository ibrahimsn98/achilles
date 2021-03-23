package me.ibrahimsn.lib.internal.connection

import me.ibrahimsn.lib.internal.lifecycle.Lifecycle
import me.ibrahimsn.lib.internal.state.Event
import me.ibrahimsn.lib.internal.state.State

sealed class ConnectionEvent : Event {

    sealed class OnLifecycle : ConnectionEvent() {

        data class StateChange<out T : Lifecycle.State> internal constructor(
            val state: T
        ) : OnLifecycle()

        object Terminate : OnLifecycle()
    }

    sealed class OnWebSocket : ConnectionEvent() {

        data class Event<out T : WebSocket.Event> internal constructor(
            val event: T
        ) : OnWebSocket()

        object Terminate : OnWebSocket()
    }

    data class OnStateChange<out T : State> internal constructor(val state: T) : ConnectionEvent()

    object OnRetry : ConnectionEvent()
}
