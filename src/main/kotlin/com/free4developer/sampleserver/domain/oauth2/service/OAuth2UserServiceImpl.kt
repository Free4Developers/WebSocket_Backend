package com.free4developer.sampleserver.domain.oauth2.service

import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.domain.member.repository.MemberRepository
import com.free4developer.sampleserver.domain.member.service.MemberService
import com.free4developer.sampleserver.domain.oauth2.entity.OAuth2Attributes
import com.free4developer.sampleserver.domain.oauth2.entity.OAuth2CustomUser
import com.free4developer.sampleserver.domain.oauth2.repository.OAuth2Repository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2UserServiceImpl(
    val oAuth2Repository: OAuth2Repository,
    val memberService: MemberService,
    val memberRepository: MemberRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val defaultUserService = DefaultOAuth2UserService()
        val oAuth2User = defaultUserService.loadUser(userRequest)
        val oAuth2Attributes = OAuth2Attributes.of(userRequest, oAuth2User)

        val member: Member = if (!oAuth2Repository.existsById(oAuth2Attributes.oauthId)) {
            oAuth2Repository.save(oAuth2Attributes)
            memberService.signupWithOAuth2(oAuth2Attributes.email, oAuth2Attributes.nickname, "", oAuth2Attributes.provider)
        } else {
            memberRepository.findByEmail(oAuth2Attributes.email)!!
        }

        return OAuth2CustomUser(oAuth2Attributes.attributes, oAuth2Attributes.attributeKey, member.id)
    }

}