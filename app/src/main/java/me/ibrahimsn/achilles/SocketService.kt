package me.ibrahimsn.achilles

import kotlinx.coroutines.flow.Flow
import me.ibrahimsn.achilleslib.annotation.Field
import me.ibrahimsn.achilleslib.annotation.ReceiveEvent
import me.ibrahimsn.achilleslib.annotation.SendEvent

interface SocketService {

    @SendEvent("echo")
    fun sendEcho(
        @Field("name") name: String,
        @Field("surname") surname: String
    )

    @ReceiveEvent("echo")
    suspend fun receiveEcho(): Flow<Response>
}
