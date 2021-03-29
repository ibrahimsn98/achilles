package me.ibrahimsn.lib.internal.service

import kotlinx.coroutines.flow.*
import me.ibrahimsn.lib.Deserialization
import me.ibrahimsn.lib.Event
import me.ibrahimsn.lib.State
import me.ibrahimsn.lib.internal.Message
import me.ibrahimsn.lib.internal.adapter.MessageAdapter
import me.ibrahimsn.lib.internal.adapter.MessageAdapterResolver
import me.ibrahimsn.lib.internal.lifecycle.Lifecycle
import me.ibrahimsn.lib.internal.webSocket.WebSocket
import java.lang.reflect.ParameterizedType

internal sealed class EventMapper<T : Any> {

    abstract fun mapToData(event: Event): Flow<T>

    object NoOp : EventMapper<Any>() {
        override fun mapToData(event: Event): Flow<Any> = flowOf(event)
    }

    class FilterEventType<E : Event>(private val clazz: Class<E>) : EventMapper<E>() {
        override fun mapToData(event: Event): Flow<E> = if (clazz.isInstance(event)) {
            @Suppress("UNCHECKED_CAST")
            flowOf (event as E)
        } else {
            emptyFlow()
        }
    }

    object ToLifecycleState : EventMapper<Lifecycle.State>() {
        private val filterEventType = FilterEventType(Event.OnLifecycle.StateChange::class.java)

        override fun mapToData(
            event: Event
        ): Flow<Lifecycle.State> = filterEventType.mapToData(event).map { it.state }
    }

    object ToWebSocketEvent : EventMapper<WebSocket.Event>() {
        private val filterEventType = FilterEventType(Event.OnWebSocket.Event::class.java)

        override fun mapToData(
            event: Event
        ): Flow<WebSocket.Event> = filterEventType.mapToData(event).map { it.event }
    }

    object ToState : EventMapper<State>() {
        private val filterEventType = FilterEventType(Event.OnStateChange::class.java)

        override fun mapToData(
            event: Event
        ): Flow<State> = filterEventType.mapToData(event).map { it.state }
    }

    class ToDeserialization<T : Any>(
        private val messageAdapter: MessageAdapter<T>
    ) : EventMapper<Deserialization<T>>() {
        private val toWebSocketEvent = ToWebSocketEvent

        override fun mapToData(event: Event): Flow<Deserialization<T>> = toWebSocketEvent.mapToData(event)
            .filter { it is WebSocket.Event.OnMessageReceived }
            .map { (it as WebSocket.Event.OnMessageReceived).message.deserialize() }

        private fun Message.deserialize(): Deserialization<T> = try {
            Deserialization.Success(messageAdapter.fromMessage(this))
        } catch (throwable: Throwable) {
            Deserialization.Error(throwable)
        }
    }

    class ToDeserializedValue<T : Any>(
        private val toDeserialization: ToDeserialization<T>
    ) : EventMapper<T>() {
        override fun mapToData(event: Event): Flow<T> = toDeserialization.mapToData(event)
            .filter { it is Deserialization.Success }
            .map { (it as Deserialization.Success).value }
    }

    class Factory(
        private val messageAdapterResolver: MessageAdapterResolver
    ) {

        private val toDeserializationCache = mutableMapOf<MessageAdapter<Any>, ToDeserialization<*>>()

        fun create(returnType: ParameterizedType, annotations: Array<Annotation>): EventMapper<*> {
            return ToLifecycleState
        }

        private fun createToDeserializationIfNeeded(messageAdapter: MessageAdapter<Any>): ToDeserialization<*> {
            if (toDeserializationCache.contains(messageAdapter)) {
                return toDeserializationCache[messageAdapter]!!
            }
            val toDeserialization = ToDeserialization(messageAdapter)
            toDeserializationCache[messageAdapter] = toDeserialization
            return toDeserialization
        }

        private fun resolveMessageAdapter(
            returnType: ParameterizedType,
            annotations: Array<Annotation>
        ): MessageAdapter<Any> {
            return messageAdapterResolver.resolve(Any::class.java, annotations)
        }
    }
}
