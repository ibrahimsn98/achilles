package me.ibrahimsn.lib.internal.client

import me.ibrahimsn.lib.internal.client.request.RequestFactory
import me.ibrahimsn.lib.internal.client.request.StaticUrlRequestFactory
import me.ibrahimsn.lib.internal.webSocket.OkHttpWebSocket
import me.ibrahimsn.lib.internal.webSocket.OkHttpWebSocketConnectionEstablisher
import me.ibrahimsn.lib.internal.webSocket.WebSocket
import okhttp3.OkHttpClient

fun OkHttpClient.newWebSocketFactory(requestFactory: RequestFactory): WebSocket.Factory {
    return OkHttpWebSocket.Factory(OkHttpWebSocketConnectionEstablisher(this, requestFactory))
}

fun OkHttpClient.newWebSocketFactory(url: String): WebSocket.Factory {
    return newWebSocketFactory(StaticUrlRequestFactory(url))
}
