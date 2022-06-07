package com.free4developer.sampleserver.config

import com.free4developer.sampleserver.domain.oauth2.service.OAuth2UserServiceImpl
import com.free4developer.sampleserver.security.FilterSkipMatcher
import com.free4developer.sampleserver.security.jwt.JwtAuthenticationFilter
import com.free4developer.sampleserver.security.jwt.JwtAuthenticationProvider
import com.free4developer.sampleserver.security.oauth.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.stream.Collectors

@EnableWebSecurity
class SecurityConfig(
    val customUserService : OAuth2UserServiceImpl,
    val customSuccessHandler : OAuth2SuccessHandler,
    val jwtAuthenticationProvider: JwtAuthenticationProvider
) : WebSecurityConfigurerAdapter() {
    companion object {
        const val REDIRECT_URI = "/oauth2/login/callback/*"
        val AUTH_WHITELIST_SWAGGER = listOf("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html/**", "/swagger-ui/**", "/webjars/**", "/swagger/**")
        val AUTH_WHITELIST_DEFAULT = listOf("/auth/**", "/oauth2/**")
        val AUTH_WHITELIST_H2_DATABASE = listOf("/h2-console/**")
    }

    override fun configure(http: HttpSecurity) {
        http {
            csrf { disable() }
            headers { frameOptions { disable() } }
            cors { configurationSource = corsConfigurationSource() }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            oauth2Login {
                redirectionEndpoint { baseUri = REDIRECT_URI }
                userInfoEndpoint { userService = customUserService }
                authenticationSuccessHandler = customSuccessHandler
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter())
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfig = CorsConfiguration()

        corsConfig.addAllowedOriginPattern("*")
        corsConfig.addAllowedHeader("*")
        corsConfig.addAllowedMethod("*")
        corsConfig.allowCredentials = true

        val urlSource = UrlBasedCorsConfigurationSource()
        urlSource.registerCorsConfiguration("/**", corsConfig)
        return urlSource
    }

    @Bean
    fun getAuthenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(jwtAuthenticationProvider)
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val skipPaths = mutableListOf<String>()
        skipPaths.addAll(AUTH_WHITELIST_DEFAULT)
        skipPaths.addAll(AUTH_WHITELIST_SWAGGER)
        skipPaths.addAll(AUTH_WHITELIST_H2_DATABASE)

        val matcher = FilterSkipMatcher(skipPaths)

        val jwtAuthenticationFilter = JwtAuthenticationFilter(matcher)
        jwtAuthenticationFilter.setAuthenticationManager(super.authenticationManager())
        return jwtAuthenticationFilter
    }
}