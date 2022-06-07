package com.free4developer.sampleserver.domain.member.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.domain.member.entity.MemberType
import com.free4developer.sampleserver.domain.member.repository.MemberRepository
import com.free4developer.sampleserver.dto.MemberDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    val memberRepository: MemberRepository
) {

    fun getMemberById(id: Long): Member {
        return memberRepository.findById(id).orElseThrow()
    }

    @Transactional
    fun signupWithOAuth2(email: String, nickname: String, password: String, memberType: String): Member {
        return memberRepository.findByEmail(email)
            ?: memberRepository.save(Member(
                    email = email,
                    nickname = nickname,
                    password = password,
                    memberType = MemberType.valueOf(memberType.uppercase())))
    }

}