package me.ibrahimsn.lib.internal.webSocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.internal.Message
import me.ibrahimsn.lib.internal.core.ShutdownReason
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString

internal class OkHttpWebSocketEventFlow(
    private val scope: CoroutineScope
) : WebSocketListener() {

    private val flow = MutableSharedFlow<WebSocket.Event>()

    fun terminate() {
        flow.cancel()
    }

    fun observe(): SharedFlow<WebSocket.Event> = flow.asSharedFlow()

    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
        scope.launch {
            flow.emit(WebSocket.Event.OnConnectionOpened(webSocket))
        }
    }

    override fun onMessage(webSocket: okhttp3.WebSocket, bytes: ByteString) {
        scope.launch {
            flow.emit(WebSocket.Event.OnMessageReceived(Message.Bytes(bytes.toByteArray())))
        }
    }

    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
        scope.launch {
            flow.emit(WebSocket.Event.OnMessageReceived(Message.Text(text)))
        }
    }

    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        scope.launch {
            flow.emit(WebSocket.Event.OnConnectionClosing(ShutdownReason(code, reason)))
        }
    }

    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        scope.launch {
            flow.emit(WebSocket.Event.OnConnectionClosed(ShutdownReason(code, reason)))
        }
    }

    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
        scope.launch {
            flow.emit(WebSocket.Event.OnConnectionFailed(t))
        }
    }
}
