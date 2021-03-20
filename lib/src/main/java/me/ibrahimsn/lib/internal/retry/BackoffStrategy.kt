package me.ibrahimsn.lib.internal.retry

interface BackoffStrategy {

    fun backoffDurationMillisAt(retryCount: Int): Long
}
