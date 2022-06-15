package com.free4developer.sampleserver.domain.chat.entity

import com.free4developer.sampleserver.common.ApplicationContextProvider
import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.domain.member.service.MemberService

class ChatRoom(
    val roomId: String
) {
    private val members = mutableSetOf<Long>()

    fun join(memberId: Long) = members.add(memberId)
    fun leave(memberId: Long) = members.remove(memberId)
    fun getMembers(): List<Member> {
        return members.map {
            ApplicationContextProvider.getBean(MemberService::class)
                .getMemberById(it)
        }
    }

}