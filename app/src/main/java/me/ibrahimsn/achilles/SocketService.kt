package me.ibrahimsn.achilles

import kotlinx.coroutines.flow.Flow
import me.ibrahimsn.lib.api.annotation.Field
import me.ibrahimsn.lib.api.annotation.ReceiveEvent
import me.ibrahimsn.lib.api.annotation.SendEvent

interface SocketService {

    @SendEvent("echo")
    fun sendEcho(
        @Field("name") name: String,
        @Field("surname") surname: String
    )

    @ReceiveEvent("echo")
    suspend fun receiveEcho(): Flow<Response>
}
