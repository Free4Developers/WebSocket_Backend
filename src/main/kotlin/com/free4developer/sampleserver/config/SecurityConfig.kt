package com.free4developer.sampleserver.config

import com.free4developer.sampleserver.domain.oauth2.service.OAuth2UserServiceImpl
import com.free4developer.sampleserver.security.jwt.JwtAuthenticationFilter
import com.free4developer.sampleserver.security.oauth.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.stream.Collectors

@EnableWebSecurity
class SecurityConfig(
    val customUserService : OAuth2UserServiceImpl,
    val customSuccessHandler : OAuth2SuccessHandler
) : WebSecurityConfigurerAdapter() {
    companion object {
        const val REDIRECT_URI = "/oauth2/login/callback/*"
        val AUTH_WHITELIST_SWAGGER = listOf("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**")
        val AUTH_WHITELIST_DEFAULT = listOf("/auth/**", "/oauth2/**")
    }

    override fun configure(http: HttpSecurity) {
        http {
            csrf { disable() }
            headers { frameOptions { disable() } }
            authorizeRequests { authorize("/h2-console", permitAll) }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            oauth2Login {
                redirectionEndpoint { baseUri = REDIRECT_URI }
                userInfoEndpoint { userService = customUserService }
                authenticationSuccessHandler = customSuccessHandler
            }
        }

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val skipPaths = mutableListOf<String>()
        skipPaths.addAll(AUTH_WHITELIST_DEFAULT)
        skipPaths.addAll(AUTH_WHITELIST_SWAGGER)

        val matcher: RequestMatcher = OrRequestMatcher(skipPaths.stream()
            .map { AntPathRequestMatcher(it) }
            .collect(Collectors.toList()) as List<RequestMatcher>)
        val jwtAuthenticationFilter = JwtAuthenticationFilter(matcher)
        jwtAuthenticationFilter.setAuthenticationManager(super.authenticationManager())
        return jwtAuthenticationFilter
    }
}