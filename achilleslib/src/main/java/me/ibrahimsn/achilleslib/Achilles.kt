package me.ibrahimsn.achilleslib

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import me.ibrahimsn.achilleslib.annotation.Field
import me.ibrahimsn.achilleslib.annotation.ReceiveEvent
import me.ibrahimsn.achilleslib.annotation.SendEvent
import me.ibrahimsn.achilleslib.exception.InvalidAnnotationException
import me.ibrahimsn.achilleslib.exception.InvalidReturnTypeException
import me.ibrahimsn.achilleslib.util.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy

class Achilles internal constructor(baseUrl: String, client: OkHttpClient,
                                    private val encodePayload: Boolean): WebSocketListener() {

    private val socket: WebSocket
    private val distributor = BehaviorSubject.create<Receiver>()

    init {
        val request = Request.Builder().url(baseUrl).build()
        socket = client.newWebSocket(request, this)
        client.dispatcher.executorService.shutdown()
    }

    @Throws(InvalidAnnotationException::class, InvalidReturnTypeException::class)
    fun <T> create(serviceInterface: Class<T>): T {
        return serviceInterface.cast(Proxy.newProxyInstance(serviceInterface.classLoader, arrayOf(serviceInterface)
        ) { _, method, args ->
            when {
                method.isAnnotationPresent(SendEvent::class.java) -> {
                    val ann = method.getAnnotation(SendEvent::class.java)
                    invokeSendMethod(ann, method, args)
                }
                method.isAnnotationPresent(ReceiveEvent::class.java) -> {
                    val ann = method.getAnnotation(ReceiveEvent::class.java)
                    invokeReceiverMethod(ann, method)
                }
                else -> throw InvalidAnnotationException("Only SendEvent and ReceiveEvent are allowed.")
            }
        })!!
    }

    private fun invokeSendMethod(ann: SendEvent, method: Method, args: Array<out Any>): Boolean {
        val payload = mutableMapOf<String, Any>()

        for ((i, par) in method.parameterAnnotations.withIndex()) {
            if (par[0] is Field) {
                payload[(par[0] as Field).value] = args[i]
            }
        }

        return socket.send(Gson().toJson(mapOf(Constants.ATTR_EVENT to ann.value,
            Constants.ATTR_DATA to if (encodePayload) encodePayload(payload) else payload)))
    }

    private fun encodePayload(payload: Map<String, Any>): String {
        return Base64.encodeToString(payload.toString().toByteArray(), Base64.DEFAULT)
    }

    @Throws(InvalidReturnTypeException::class)
    private fun invokeReceiverMethod(ann: ReceiveEvent, method: Method): Any {
        if (method.returnType != Observable::class.java)
            throw InvalidReturnTypeException("Only observable return type is allowed.")

        return distributor
            .filter { it.event == ann.value }
            .map {
                val json = JsonParser.parseString(it.data.toString()).asJsonObject

                val typeArg = (method.genericReturnType as ParameterizedType).actualTypeArguments[0]
                Gson().fromJson(json, typeArg as Class<*>)
            }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val json = JsonParser.parseString(text).asJsonObject

        if (json.has(Constants.ATTR_EVENT) && json.has(Constants.ATTR_DATA))
            distributor.onNext(Gson().fromJson(json, Receiver::class.java))
    }

    class Builder {
        private var baseUrl = Constants.TEST_URL
        private var client: OkHttpClient = OkHttpClient().newBuilder().build()
        private var encodePayload = false

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun client(client: OkHttpClient): Builder {
            this.client = client
            return this
        }

        fun encodePayload(encodePayload: Boolean): Builder {
            this.encodePayload = encodePayload
            return this
        }

        fun build() = Achilles(baseUrl, client, encodePayload)
    }
}