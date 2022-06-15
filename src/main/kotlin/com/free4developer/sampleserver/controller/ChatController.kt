package com.free4developer.sampleserver.controller

import com.free4developer.sampleserver.dto.ChatMessageDto
import lombok.RequiredArgsConstructor
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ChatController(
    val template: SimpMessagingTemplate
) {
    @MessageMapping("/chat/enter")
    fun enter(message: ChatMessageDto) {
        message.message = "${message.writerNickname} 님이 입장하셨습니다."
        template.convertAndSend("/channel/room/open", message)
    }

    @MessageMapping("/chat/message")
    fun sendMessage(message: ChatMessageDto) {
        template.convertAndSend("/channel/room/open", message)
    }

}