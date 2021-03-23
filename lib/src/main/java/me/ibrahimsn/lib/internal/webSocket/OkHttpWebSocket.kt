package me.ibrahimsn.lib.internal.webSocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import me.ibrahimsn.lib.internal.Message
import me.ibrahimsn.lib.internal.core.ShutdownReason
import okhttp3.WebSocketListener
import okio.ByteString.Companion.toByteString

class OkHttpWebSocket internal constructor(
    private val okHttpWebSocketEventFlow: OkHttpWebSocketEventFlow,
    private val connectionEstablisher: ConnectionEstablisher
) : WebSocket {

    private var webSocket: okhttp3.WebSocket? = null

    override fun open(): Flow<WebSocket.Event> = okHttpWebSocketEventFlow.observe()
        .onSubscription {
            connectionEstablisher.establishConnection(okHttpWebSocketEventFlow)
        }.onEach {
            handleWebSocketEvent(it)
        }

    @Synchronized
    override fun send(message: Message): Boolean = when (message) {
        is Message.Text -> webSocket?.send(message.value) ?: false
        is Message.Bytes -> {
            val bytes = message.value
            val byteString = bytes.toByteString(0, bytes.size)
            webSocket?.send(byteString) ?: false
        }
    }

    @Synchronized
    override fun close(shutdownReason: ShutdownReason): Boolean {
        val (code, reasonText) = shutdownReason
        return webSocket?.close(code, reasonText) ?: false
    }

    @Synchronized
    override fun cancel() = webSocket?.cancel() ?: Unit

    private fun handleWebSocketEvent(event: WebSocket.Event) {
        when (event) {
            is WebSocket.Event.OnConnectionOpened<*> -> webSocket = event.webSocket as okhttp3.WebSocket
            is WebSocket.Event.OnConnectionClosing -> close(ShutdownReason.GRACEFUL)
            is WebSocket.Event.OnConnectionClosed, is WebSocket.Event.OnConnectionFailed -> handleConnectionShutdown()
        }
    }

    @Synchronized
    private fun handleConnectionShutdown() {
        webSocket = null
        okHttpWebSocketEventFlow.terminate()
    }

    interface ConnectionEstablisher {
        fun establishConnection(webSocketListener: WebSocketListener)
    }

    class Factory(
        private val connectionEstablisher: ConnectionEstablisher
    ) : WebSocket.Factory {
        override fun create(): WebSocket =
            OkHttpWebSocket(OkHttpWebSocketEventFlow(), connectionEstablisher)
    }
}
