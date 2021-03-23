package me.ibrahimsn.lib.internal.webSocket

import me.ibrahimsn.lib.internal.client.request.RequestFactory
import okhttp3.OkHttpClient
import okhttp3.WebSocketListener

class OkHttpWebSocketConnectionEstablisher(
    private val okHttpClient: OkHttpClient,
    private val requestFactory: RequestFactory
) : OkHttpWebSocket.ConnectionEstablisher {

    override fun establishConnection(webSocketListener: WebSocketListener) {
        val request = requestFactory.createRequest()
        okHttpClient.newWebSocket(request, webSocketListener)
    }
}
