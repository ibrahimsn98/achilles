package me.ibrahimsn.lib.internal.lifecycle

internal fun Lifecycle.State.combine(other: Lifecycle.State): Lifecycle.State {
    if (this.isStoppedAndAborted() || other.isStoppedAndAborted()) {
        return Lifecycle.State.Stopped.AndAborted
    }
    if (this.isStopped()) {
        return this
    }
    if (other.isStopped()) {
        return other
    }
    return Lifecycle.State.Started
}

internal fun Lifecycle.State.isEquivalentTo(other: Lifecycle.State): Boolean =
    this == other || isStopped() && other.isStopped()

private fun Lifecycle.State.isStopped(): Boolean =
    this is Lifecycle.State.Stopped

private fun Lifecycle.State.isStoppedAndAborted(): Boolean =
    this == Lifecycle.State.Stopped.AndAborted