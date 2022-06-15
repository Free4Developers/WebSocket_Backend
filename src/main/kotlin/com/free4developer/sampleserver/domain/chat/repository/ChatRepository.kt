package com.free4developer.sampleserver.domain.chat.repository

import com.free4developer.sampleserver.domain.chat.entity.ChatRoom
import org.springframework.stereotype.Repository

@Repository
class ChatRepository {
    val rooms = mutableMapOf<String, ChatRoom>()

    init {
        this.createChatRoom("open")
    }

    fun findChatRoomById(roomId: String)
        = rooms[roomId] ?: throw RuntimeException("존재하지 않는 채팅방입니다. (roomId : ${roomId})")

    fun createChatRoom(roomId: String): ChatRoom {
        rooms[roomId] = ChatRoom(roomId)
        return rooms[roomId]!!
    }

}