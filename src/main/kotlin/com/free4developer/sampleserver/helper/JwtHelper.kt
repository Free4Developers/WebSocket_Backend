package com.free4developer.sampleserver.helper

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtHelper(
    @Value("\${jwt.secret-key}")
    val secretKey: String
) {
    companion object {
        const val ACCESS_TOKEN_VALIDITY = 1800000L // 30 minutes
        const val REFRESH_TOKEN_VALIDITY = 1209600000L // 2 weeks
    }

    fun generateJwt(id: Long, jwtType: JwtType): String {
        val claims = mutableMapOf<String, Any>()
        claims["jwt_type"] = jwtType.name

        val issuedAt = Date(System.currentTimeMillis())
        val validity = Date(System.currentTimeMillis() + when(jwtType) {
            JwtType.ACCESS_TOKEN -> ACCESS_TOKEN_VALIDITY
            JwtType.REFRESH_TOKEN -> REFRESH_TOKEN_VALIDITY
        })

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(id.toString())
            .setIssuedAt(issuedAt)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun validateJwt(jwtToken: String): Boolean {
        return Jwts.parser().setSigningKey(secretKey).isSigned(jwtToken)
    }

    fun <T> getClaim(jwtToken: String, claimResolver: (Claims) -> (T)): T {
        val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).body
        return claimResolver(claims)
    }
}

enum class JwtType {
    ACCESS_TOKEN, REFRESH_TOKEN
}