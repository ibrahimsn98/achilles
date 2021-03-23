package me.ibrahimsn.lib.internal.session

import kotlinx.coroutines.Job
import me.ibrahimsn.lib.internal.webSocket.WebSocket

internal data class Session(val webSocket: WebSocket, val webSocketJob: Job)
