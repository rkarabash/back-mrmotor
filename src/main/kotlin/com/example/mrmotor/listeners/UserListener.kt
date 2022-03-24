package com.example.mrmotor.listeners

import com.example.mrmotor.models.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

class UserListener {
    @PrePersist
//    @PreUpdate
    fun hashPassword(user: User) {
//        val regex = """\\A\\${'$'}2(a|y|b)?\\${'$'}(\\d\\d)\\${'$'}[./0-9A-Za-z]{53}""".toRegex()
//        if (!regex.matches(user.password))
//
        user.password = BCryptPasswordEncoder().encode(user.password)
    }
}