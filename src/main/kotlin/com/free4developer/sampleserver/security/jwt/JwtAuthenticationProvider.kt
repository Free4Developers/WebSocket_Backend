package com.free4developer.sampleserver.security.jwt

import com.free4developer.sampleserver.domain.member.service.MemberService
import com.free4developer.sampleserver.helper.JwtHelper
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    val memberService: MemberService,
    val jwtHelper: JwtHelper
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        val accessToken: String = (authentication as JwtAuthenticationToken).getAuthorizationHeader()

        if (!jwtHelper.validateJwt(accessToken)) {
            throw java.lang.RuntimeException("Invalid JWT token information.")
        }

        val id: Long = jwtHelper.getClaim(accessToken, Claims::getSubject).toLong()
        val member = memberService.getMemberById(id)
        return JwtAuthenticationToken.newAfterJwtAuthenticationToken(member)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JwtAuthenticationToken::class.java
    }

}