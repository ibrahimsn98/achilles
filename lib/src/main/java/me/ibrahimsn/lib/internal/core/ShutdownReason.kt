package me.ibrahimsn.lib.internal.core

data class ShutdownReason(val code: Int, val reason: String) {
    companion object {
        private val NORMAL_CLOSURE_STATUS_CODE = 1000
        private val NORMAL_CLOSURE_REASON = "Normal closure"

        @JvmField
        val GRACEFUL = ShutdownReason(NORMAL_CLOSURE_STATUS_CODE, NORMAL_CLOSURE_REASON)
    }
}
