package me.ibrahimsn.lib.internal.connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import me.ibrahimsn.lib.internal.WebSocket
import me.ibrahimsn.lib.internal.lifecycle.Lifecycle
import me.ibrahimsn.lib.internal.retry.BackoffStrategy
import me.ibrahimsn.lib.internal.session.Session
import me.ibrahimsn.lib.internal.state.StateMachine

internal class Connection(
    private val lifecycle: Lifecycle,
    private val scope: CoroutineScope,
    private val webSocketFactory: WebSocket.Factory,
    private val backoffStrategy: BackoffStrategy,
) {

    private val stateMachine = StateMachine<ConnectionEvent, ConnectionState>(
        ConnectionState.Disconnected,
        scope,
    ) { event, state ->
        when (state) {
            is ConnectionState.Disconnected -> {
                when (event) {
                    is
                }
            }
            is ConnectionState.WaitingToRetry -> {

            }
            is ConnectionState.Connecting -> {

            }
            is ConnectionState.Connected -> {

            }
            is ConnectionState.Disconnecting -> {

            }
            is ConnectionState.Destroyed -> {

            }
        }
    }


    class Factory(
        private val lifecycle: Lifecycle,
        private val scope: CoroutineScope,
        private val webSocketFactory: WebSocket.Factory,
        private val backoffStrategy: BackoffStrategy
    ) {
        fun create(): Connection {
            return Connection(lifecycle, scope, webSocketFactory, backoffStrategy)
        }
    }
}
