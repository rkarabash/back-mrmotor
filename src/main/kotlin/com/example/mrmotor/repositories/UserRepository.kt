package com.example.mrmotor.repositories

import com.example.mrmotor.models.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
}