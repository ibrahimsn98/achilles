package me.ibrahimsn.lib.internal.session

import kotlinx.coroutines.Job
import okhttp3.WebSocket

internal data class Session(val webSocket: WebSocket, val webSocketJob: Job)
