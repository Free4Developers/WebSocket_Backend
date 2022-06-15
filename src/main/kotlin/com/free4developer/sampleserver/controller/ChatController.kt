package com.free4developer.sampleserver.controller

import com.free4developer.sampleserver.domain.chat.repository.ChatRepository
import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.dto.ChatMessageDto
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ChatController(
    val template: SimpMessagingTemplate,
    val chatRepository: ChatRepository
) {
    @GetMapping("/chat/room/{roomId}/members")
    fun findEnteredMembers(@PathVariable roomId: String): List<Member> {
        return chatRepository.findChatRoomById(roomId).getMembers()
    }

    @MessageMapping("/chat/enter")
    fun enter(message: ChatMessageDto) {
        message.message = "${message.writerNickname} 님이 입장하셨습니다."
        chatRepository.findChatRoomById(message.roomId)
            .join(message.writerId)

        template.convertAndSend("/channel/chat/room/" + message.roomId, message)
    }

    @MessageMapping("/chat/leave")
    fun leave(message: ChatMessageDto) {
        message.message = "${message.writerNickname} 님이 퇴장하셨습니다."
        chatRepository.findChatRoomById(message.roomId)
            .leave(message.writerId)

        template.convertAndSend("/channel/chat/room/" + message.roomId, message)
    }

    @MessageMapping("/chat/message")
    fun sendMessage(message: ChatMessageDto) {
        // TODO : Nickname 초기화 안되는지 반드시 테스트 후 확인!!
        template.convertAndSend("/channel/chat/room/" + message.roomId, message)
    }

}