package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizResult
import org.springframework.data.repository.CrudRepository

interface QuizResultRepository: CrudRepository<QuizResult, Long> {
    fun findByUserIdAndQuizId(userId: Long, quizId: Long): QuizResult?
    fun findByUserId(userId: Long): List<QuizResult>
    fun findByQuizId(quizId: Long): List<QuizResult>
}