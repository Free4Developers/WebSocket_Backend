package com.free4developer.sampleserver.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val matcher: RequestMatcher
): AbstractAuthenticationProcessingFilter(DEFAULT_FILTER_PROCESSES_URI) {
    companion object {
        const val DEFAULT_FILTER_PROCESSES_URI = "/**"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val AUTHORIZATION_TYPE = "Bearer"
    }

    override fun requiresAuthentication(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        return !matcher.matches(request)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)?.substring(AUTHORIZATION_TYPE.length + 1) ?: throw RuntimeException()
        return JwtAuthenticationToken.newBeforeJwtAuthenticationToken(authorizationHeader)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        super.unsuccessfulAuthentication(request, response, failed)
        response.status = HttpStatus.UNAUTHORIZED.value()
    }
}