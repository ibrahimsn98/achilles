package me.ibrahimsn.lib.internal.adapter

import me.ibrahimsn.lib.internal.Message
import java.lang.reflect.Type

interface MessageAdapter<T> {

    fun fromMessage(message: Message): T

    fun toMessage(data: T): Message

    interface Factory {

        fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*>
    }
}
