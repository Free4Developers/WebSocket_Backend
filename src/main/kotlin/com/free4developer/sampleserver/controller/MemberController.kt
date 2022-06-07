package com.free4developer.sampleserver.controller

import com.free4developer.sampleserver.domain.member.service.MemberService
import com.free4developer.sampleserver.dto.MemberDto
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
class MemberController(
    val memberService: MemberService
) {

    @GetMapping("/{id}")
    fun findMemberById(@PathVariable id: Long): MemberDto {
        return memberService.findMemberById(id)
    }

}