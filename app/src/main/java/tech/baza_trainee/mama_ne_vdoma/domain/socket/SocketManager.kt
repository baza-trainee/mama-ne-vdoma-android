package tech.baza_trainee.mama_ne_vdoma.domain.socket

import kotlinx.coroutines.flow.Flow

interface SocketManager {

    val messages: Flow<List<ChatMessage>>

    val message: Flow<ChatMessage>

    fun connect()

    fun disconnect()

    fun isConnected(): Boolean

    fun getMessages(groupId: String, startDate: String = "")

    fun sendMessage(groupId: String, message: String)
}