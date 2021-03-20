package me.ibrahimsn.lib.internal.retry

class LinearBackoffStrategy(
    val durationMillis: Long
) : BackoffStrategy {

    init {
        require(durationMillis > 0) { "durationMillis, $durationMillis, must be positive" }
    }

    override fun backoffDurationMillisAt(retryCount: Int): Long = durationMillis
}
