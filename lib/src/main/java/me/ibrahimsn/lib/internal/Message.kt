package me.ibrahimsn.lib.internal

sealed class Message {

    data class Text(val value: String) : Message()

    class Bytes(val value: ByteArray) : Message() {
        operator fun component1(): ByteArray = value
    }
}
