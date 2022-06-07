package com.free4developer.sampleserver.controller

import com.free4developer.sampleserver.domain.member.repository.MemberRepository
import com.free4developer.sampleserver.domain.member.service.MemberService
import com.free4developer.sampleserver.dto.MemberDto
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
class MemberController(
    val memberRepository: MemberRepository
) {

    @GetMapping("/{id}")
    fun retrieveMember(@PathVariable id: Long): ResponseEntity<MemberDto> {
        return ResponseEntity.ok(memberRepository.findMemberById(id))
    }

}