package me.ibrahimsn.lib.internal.client.request

import okhttp3.Request

internal class StaticUrlRequestFactory(
    private val url: String
) : RequestFactory {

    override fun createRequest(): Request = Request.Builder()
        .url(url)
        .build()
}
