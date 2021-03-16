package me.ibrahimsn.achilleslib.internal.state

import kotlinx.coroutines.Job
import me.ibrahimsn.achilleslib.internal.session.Session

sealed class State {

    data class WaitingToRetry internal constructor(
        internal val timerJob: Job,
        val retryCount: Int,
        val retryInMillis: Long
    ) : State()

    data class Connecting internal constructor(
        internal val session: Session,
        val retryCount: Int
    ) : State()

    data class Connected internal constructor(
        internal val session: Session
    ) : State()

    object Disconnecting : State()

    object Disconnected : State()

    object Destroyed : State()
}
