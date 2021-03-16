package me.ibrahimsn.achilleslib.api.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Field(val value: String)
