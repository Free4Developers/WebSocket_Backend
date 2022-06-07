package com.free4developer.sampleserver.security.jwt

import com.free4developer.sampleserver.domain.member.entity.Member
import org.springframework.security.authentication.AbstractAuthenticationToken
import javax.transaction.NotSupportedException

class JwtAuthenticationToken: AbstractAuthenticationToken {
    private var member: Member? = null
    private var authorizationHeader: String? = null

    constructor(member: Member): super(null) {
        this.member = member
        super.setAuthenticated(true)
    }
    constructor(authorizationHeader: String): super(null) {
        this.authorizationHeader = authorizationHeader
        super.setAuthenticated(false)
    }

    companion object {
        fun newBeforeJwtAuthenticationToken(authorizationHeader: String): JwtAuthenticationToken = JwtAuthenticationToken(authorizationHeader)
        fun newAfterJwtAuthenticationToken(member: Member): JwtAuthenticationToken = JwtAuthenticationToken(member)
    }

    override fun getCredentials(): Nothing {
        throw NotSupportedException()
    }

    override fun getPrincipal(): Member {
        return this.member ?: throw NotSupportedException("인증 전 객체는 지원하지 않는 메소드입니다.")
    }

    fun getAuthorizationHeader(): String = this.authorizationHeader ?: throw NotSupportedException("인증 후 객체는 지원하지 않는 메소드입니다.")
}