package me.ibrahimsn.achilleslib.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SendEvent(val value: String)
