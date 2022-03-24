package com.example.mrmotor.repositories

import com.example.mrmotor.models.Quiz
import org.springframework.data.repository.CrudRepository

interface QuizRepository: CrudRepository<Quiz, Long> {
    fun findByAuthorId(authorId: Long): List<Quiz>
}