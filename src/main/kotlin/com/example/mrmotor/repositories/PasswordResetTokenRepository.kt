package com.example.mrmotor.repositories

import com.example.mrmotor.models.PasswordResetToken
import org.springframework.data.repository.CrudRepository

interface PasswordResetTokenRepository: CrudRepository<PasswordResetToken, Long> {
    fun findByToken(token: String): PasswordResetToken?
}