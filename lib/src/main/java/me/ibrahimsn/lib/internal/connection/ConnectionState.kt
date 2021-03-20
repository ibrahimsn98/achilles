package me.ibrahimsn.lib.internal.connection

import kotlinx.coroutines.Job
import me.ibrahimsn.lib.internal.session.Session
import me.ibrahimsn.lib.internal.state.State

sealed class ConnectionState : State {

    data class WaitingToRetry internal constructor(
        internal val timerJob: Job,
        val retryCount: Int,
        val retryInMillis: Long
    ) : ConnectionState()

    data class Connecting internal constructor(
        internal val session: Session,
        val retryCount: Int
    ) : ConnectionState()

    data class Connected internal constructor(
        internal val session: Session
    ) : ConnectionState()

    object Disconnecting : ConnectionState()

    object Disconnected : ConnectionState()

    object Destroyed : ConnectionState()
}