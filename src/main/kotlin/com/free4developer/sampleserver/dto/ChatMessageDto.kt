package com.free4developer.sampleserver.dto

import com.free4developer.sampleserver.common.ApplicationContextProvider
import com.free4developer.sampleserver.domain.member.service.MemberService

data class ChatMessageDto(
    var roomId: String,
    var writerId: Long,
    var message: String?
) {
    val writerNickname: String by lazy {
        if (writerId == -1L) {
            "Server"
        } else {
            ApplicationContextProvider.getBean(MemberService::class)
                .getMemberById(writerId)
                .nickname
        }
    }
}