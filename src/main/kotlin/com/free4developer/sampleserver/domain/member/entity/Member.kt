package com.free4developer.sampleserver.domain.member.entity

import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.*

@Entity
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val email: String,
    val nickname: String,
    val password: String,

    @Enumerated(EnumType.STRING)
    val memberType: MemberType = MemberType.EMAIL
) {
    fun validatePassword(passwordEncoder: PasswordEncoder, inputPassword: String): Boolean {
        return passwordEncoder.matches(inputPassword, this.password)
    }
}