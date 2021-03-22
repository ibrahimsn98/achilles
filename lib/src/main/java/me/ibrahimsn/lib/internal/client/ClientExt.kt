package me.ibrahimsn.lib.internal.client

import me.ibrahimsn.lib.internal.WebSocket
import me.ibrahimsn.lib.internal.client.request.RequestFactory
import okhttp3.OkHttpClient

fun OkHttpClient.newWebSocketFactory(requestFactory: RequestFactory): WebSocket.Factory {
    return OkHttpWebSocket.Factory(OkHttpClientWebSocketConnectionEstablisher(this, requestFactory))
}

fun OkHttpClient.newWebSocketFactory(url: String): WebSocket.Factory {
    return newWebSocketFactory(StaticUrlRequestFactory(url))
}
