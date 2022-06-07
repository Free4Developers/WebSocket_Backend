package com.free4developer.sampleserver.controller

import com.free4developer.sampleserver.domain.member.entity.Member
import com.free4developer.sampleserver.domain.member.entity.MemberType
import com.free4developer.sampleserver.domain.member.repository.MemberRepository
import com.free4developer.sampleserver.dto.SigninDto
import com.free4developer.sampleserver.dto.SignupDto
import com.free4developer.sampleserver.helper.JwtHelper
import com.free4developer.sampleserver.helper.JwtType
import com.free4developer.sampleserver.helper.setCookie
import io.jsonwebtoken.Claims
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(
    val memberRepository: MemberRepository,
    val jwtHelper: JwtHelper
) {
    private val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @PostMapping("/signup")
    fun signup(@RequestBody signupDto: SignupDto) {
        if (memberRepository.existsByEmail(signupDto.email)) {
            throw java.lang.RuntimeException("이미 가입되어있는 이메일입니다.")
        }
        memberRepository.save(Member(
            email = signupDto.email,
            nickname = signupDto.nickname,
            password = passwordEncoder.encode(signupDto.password)
        ))
    }

    @PostMapping("/signin")
    fun signin(response: HttpServletResponse,
               @RequestBody signinDto: SigninDto): ResponseEntity<String> {
        val member = memberRepository.findByEmailAndMemberType(signinDto.email, MemberType.EMAIL)
            ?: throw java.lang.RuntimeException("존재하지 않는 사용자입니다.")

        if (!member.validatePassword(passwordEncoder, signinDto.password)) {
            throw java.lang.RuntimeException("잘못된 패스워드입니다.")
        }
        val accessToken = jwtHelper.generateJwt(member.id, JwtType.ACCESS_TOKEN)
        val refreshToken = jwtHelper.generateJwt(member.id, JwtType.REFRESH_TOKEN)

        response.setCookie(
            name = "RefreshToken",
            value = refreshToken,
            expireDate = jwtHelper.getClaim(refreshToken, Claims::getExpiration)
        )
        return ResponseEntity.ok(accessToken)
    }

}