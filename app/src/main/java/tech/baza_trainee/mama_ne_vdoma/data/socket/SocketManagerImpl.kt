package tech.baza_trainee.mama_ne_vdoma.data.socket

import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import org.json.JSONArray
import org.json.JSONObject
import tech.baza_trainee.mama_ne_vdoma.BuildConfig
import tech.baza_trainee.mama_ne_vdoma.domain.preferences.UserPreferencesDatastoreManager
import tech.baza_trainee.mama_ne_vdoma.domain.socket.ChatMessage
import tech.baza_trainee.mama_ne_vdoma.domain.socket.SocketManager
import java.net.URI

class SocketManagerImpl(
    private val gson: Gson,
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager
): SocketManager {

    override val messages = MutableSharedFlow<List<ChatMessage>>(
        replay = 0,
        extraBufferCapacity = 5
    )

    override val message = MutableSharedFlow<ChatMessage>(
        replay = 0,
        extraBufferCapacity = 5
    )

    private var socket: Socket? = null

    init {
        val sessionId = userPreferencesDatastoreManager.cookies
            .find { it.contains(SESSION_ID_HEADER) }
            ?.let { s ->
                s.split(";")
                    .find { it.startsWith(SESSION_ID_HEADER) }
                    ?.substring(SESSION_ID_HEADER.length)
            }.orEmpty()

        val uri = URI.create(BASE)
        val options = IO.Options.builder()
            .setAuth(
                mapOf(SESSION_ID to sessionId)
            )
            .setPath(
                if (BuildConfig.DEBUG) DEBUG_NSP
                else PROD_NSP
            )
            .build()

        socket = IO.socket(uri, options)

        socket?.on(GET_MESSAGES) { args ->
            val newMessages = mutableListOf<ChatMessage>()

            (args[0] as JSONArray).apply {
                for (index in 0 until length()) {
                    val message = gson.fromJson(get(index).toString(), ChatMessage::class.java)

                    newMessages.add(message)
                }
            }

            messages.tryEmit(newMessages)
        }

        socket?.on(MESSAGE) { args ->
            val newMessage = gson.fromJson(
                (args[0] as JSONObject).toString(),
                ChatMessage::class.java
            )

            message.tryEmit(newMessage)
        }

        socket?.on("exception") { args ->
            val errors = mutableListOf<String>()

            (args[0] as JSONArray).apply {
                for (index in 0 until length()) {
                    val error = get(index) as String

                    errors.add(error)
                }
            }

            errors.forEach { error ->
                message.tryEmit(
                    ChatMessage(
                        id = SocketManager.ERROR_TAG,
                        message = error
                    )
                )
            }
        }

        socket?.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "connected")
        }

        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "disconnected")
        }

        socket?.on("auth") {
            Log.d(TAG, "authorized")
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) {
            Log.d(TAG, "connection error")
        }
    }

    override fun connect() {
        socket?.connected()?.let {
            if (!it) socket?.connect()
        }
    }

    override fun disconnect() {
        socket?.disconnect()
    }

    override fun isConnected() = socket?.connected() ?: false

    override fun getMessages(groupId: String, startDate: String) {
        socket?.emit(
            GET_MESSAGES,
            JSONObject().apply {
                put("chatId", groupId)
                if (startDate.isNotEmpty()) put("from", startDate)
            }
        )
    }

    override fun sendMessage(groupId: String, message: String) {
        socket?.emit(
            MESSAGE,
            JSONObject().apply {
                put("groupId", groupId)
                put("message", message)
            }
        )
    }

    companion object {

        private const val MESSAGE = "message"
        private const val GET_MESSAGES = "get-messages"
        private const val SESSION_ID = "sessionId"
        private const val SESSION_ID_HEADER = "connect.sid="

        private const val BASE = "https://mama-ne-vdoma.online"
        private const val DEBUG_NSP = "/stage/socket.io"
        private const val PROD_NSP = "/back/socket.io"

        private const val TAG = "Socket"
    }
}
