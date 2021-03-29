package me.ibrahimsn.lib

sealed class Deserialization<T> {

    data class Success<T>(val value: T) : Deserialization<T>()

    data class Error<T>(val throwable: Throwable) : Deserialization<T>()
}
