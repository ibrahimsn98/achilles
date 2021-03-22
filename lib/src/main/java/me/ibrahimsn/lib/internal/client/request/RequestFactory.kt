package me.ibrahimsn.lib.internal.client.request

import okhttp3.Request

interface RequestFactory {
    
    fun createRequest(): Request
}
