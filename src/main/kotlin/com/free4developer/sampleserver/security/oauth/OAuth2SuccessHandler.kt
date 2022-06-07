package com.free4developer.sampleserver.security.oauth

import com.free4developer.sampleserver.domain.oauth2.entity.OAuth2CustomUser
import com.free4developer.sampleserver.helper.JwtHelper
import com.free4developer.sampleserver.helper.JwtType
import com.free4developer.sampleserver.helper.setCookie
import io.jsonwebtoken.Claims
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2SuccessHandler(
    val jwtHelper: JwtHelper
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User: OAuth2CustomUser = authentication.principal as OAuth2CustomUser
        val accessToken = jwtHelper.generateJwt(oAuth2User.memberId, JwtType.ACCESS_TOKEN)
        val refreshToken = jwtHelper.generateJwt(oAuth2User.memberId, JwtType.REFRESH_TOKEN)

        response.setCookie(
            name = "RefreshToken",
            value = refreshToken,
            expireDate = jwtHelper.getClaim(refreshToken, Claims::getExpiration)
        )
        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.println(accessToken)
    }

}
