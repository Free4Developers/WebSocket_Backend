package com.free4developer.sampleserver.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val matcher: RequestMatcher
): AbstractAuthenticationProcessingFilter(matcher) {
    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val AUTHORIZATION_TYPE = "Bearer"
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)?.substring(AUTHORIZATION_TYPE.length + 1) ?: throw RuntimeException("Not Found JWT token information.")
        val authToken = JwtAuthenticationToken.newBeforeJwtAuthenticationToken(authorizationHeader)
        return super.getAuthenticationManager().authenticate(authToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authResult
        SecurityContextHolder.setContext(context)
        chain.doFilter(request, response)
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