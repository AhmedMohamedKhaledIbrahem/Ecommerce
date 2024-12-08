package com.example.ecommerce.core.network.websocket

import android.util.Log
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.utils.EmulatorDevice
import com.example.ecommerce.features.userprofile.domain.entites.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WebSocketEcommerce @Inject constructor() : WebSocketListener(), SendProfileNameDetails {
    private  var client: OkHttpClient = OkHttpClient()
    private lateinit var webSocket: WebSocket

    private val webSocketUrl = "ws://10.0.2.2:8080"
    private val webSocketUrl2 = "ws://10.0.0.105:8080?userId=1"
    private val webSocketUrlCheckDevice = if (EmulatorDevice.isEmulator()) webSocketUrl else webSocketUrl2
    fun start() {
        val request = Request.Builder().url(webSocketUrlCheckDevice).build()
        webSocket = client.newWebSocket(request, this)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.e("webSocket", "onClosed: the webSocket onClosed ")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        Log.e("webSocket", "onClosing: the webSocket onClosing ")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("webSocket", "WebSocket failure: ${t.message}")
        throw FailureException("${t.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val jsonObject = JSONObject(text)
        Log.e("webSocket", "Message received: ${jsonObject.toString()}")
        val message = jsonObject.getString("type")
        Log.e("type",message.toString())
        /*val jsonObject = JSONObject(text)

        // Check the type of message (simple_message in this case)
        val messageType = jsonObject.getString("type")

        if (messageType == "simple_message") {
            val message = jsonObject.getString("message")
            Log.e("webSocket", "Received simple message: $message")
        }
        val message = jsonObject.getString("message")

        Log.e("webSocket", "Received simple message: $message")
      */
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        val jsonObject = JSONObject(bytes.utf8())
        Log.e("webSocket", "Message received: ${jsonObject.toString()}")
        /*Log.e("webSocket", "Message received: $bytes")
        val jsonObject = JSONObject(bytes.utf8())

        // Check the type of message (profile update in this case)
        val messageType = jsonObject.getString("type")

        if (messageType == "profile_update") {
            val userId = jsonObject.getString("userId")
            val firstName = jsonObject.getString("firstName")
            val lastName = jsonObject.getString("lastName")
            val displayName = jsonObject.getString("displayName")
            Log.e("TAG", "onMessage: $userId", )
            Log.e("TAG", "onMessage: $firstName", )
            Log.e("TAG", "onMessage: $lastName", )
            Log.e("TAG", "onMessage: $displayName", )

        }else{
            Log.e("false", "onMessage: false", )
        }*/
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.e("webSocket", "onOpen: the webSocket connect ")
    }

    override suspend fun sendProfileNameDetailsUpdate(
        updateUserProfileEntity: Entity
    ) {
        withContext(Dispatchers.IO) {
            val json = JSONObject().apply {
                put("type", "profile_update")
                put("userid", updateUserProfileEntity.userId)
                put("firstName", updateUserProfileEntity.firstName)
                put("lastName", updateUserProfileEntity.lastName)
                put("displayName", updateUserProfileEntity.displayName)
            }
            webSocket.send(json.toString())
        }
    }

    fun onDestroy(){
        webSocket.close(1000,"Client closing")
        client.dispatcher.executorService.shutdown()
    }


}