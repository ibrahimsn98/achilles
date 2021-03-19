package me.ibrahimsn.lib.api.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ReceiveEvent(val value: String)
