package com.free4developer.sampleserver.domain.member.repository

import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.domain.member.entity.MemberType
import com.free4developer.sampleserver.dto.MemberDto
import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<Member, Long> {
    fun findMemberById(id: Long): MemberDto
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Member?
    fun findByEmailAndMemberType(email: String, memberType: MemberType): Member?
}