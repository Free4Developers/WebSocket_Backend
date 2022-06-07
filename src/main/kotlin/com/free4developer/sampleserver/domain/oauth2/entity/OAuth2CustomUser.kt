package com.free4developer.sampleserver.domain.oauth2.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable

data class OAuth2CustomUser(
    private val attributes: MutableMap<String, Any>,
    private val nameAttributeKey: String,
    val memberId: Long
): OAuth2User, Serializable {

    override fun getName(): String {
        return this.attributes[this.nameAttributeKey].toString()
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return this.attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }
}