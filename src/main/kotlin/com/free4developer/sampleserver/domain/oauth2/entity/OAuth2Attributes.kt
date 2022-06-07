package com.free4developer.sampleserver.domain.oauth2.entity

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "oauth2_member")
data class OAuth2Attributes(
    @Id
    val oauthId: Long,

    @Transient
    val attributeKey: String,

    @Transient
    val attributes: MutableMap<String, Any>,

    val provider: String,
    val nickname: String,
    val email: String
) {

    companion object {
        const val KAKAO = "kakao"

        fun of(userRequest : OAuth2UserRequest, oAuth2User : OAuth2User) : OAuth2Attributes {
            val provider = userRequest.clientRegistration.registrationId
            val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

            return when(provider) {
                KAKAO -> ofKakao(provider, userNameAttributeName, oAuth2User.attributes,
                    oAuth2User.attributes[userNameAttributeName] as Long
                )
                else -> throw java.lang.IllegalArgumentException()
            }
        }

        fun ofKakao(provider: String, attributeKey: String, attributes: MutableMap<String, Any>, oauthId: Long) : OAuth2Attributes {
            val profile = attributes["properties"] as Map<*, *>
            val account = attributes["kakao_account"] as Map<*, *>

            val nickname = profile["nickname"] ?: ""
            val email = account["email"] ?: ""

            return OAuth2Attributes(
                oauthId = oauthId,
                provider = provider,
                nickname = nickname as String,
                email = email as String,
                attributes = attributes,
                attributeKey = attributeKey
            )
        }
    }

}
