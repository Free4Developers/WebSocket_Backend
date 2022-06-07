package com.free4developer.sampleserver.security

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class FilterSkipMatcher(
    skipPaths: List<String>
) : RequestMatcher {
    private val matcher: OrRequestMatcher

    init {
        val requestMatcher: List<RequestMatcher> = skipPaths.stream()
            .map { AntPathRequestMatcher(it) }
            .collect(Collectors.toList())
        this.matcher = OrRequestMatcher(requestMatcher)
    }

    override fun matches(request: HttpServletRequest): Boolean {
        return !matcher.matches(request)
    }
}