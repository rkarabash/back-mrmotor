package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizAnswer
import org.springframework.data.repository.CrudRepository

interface QuizAnswerRepository: CrudRepository<QuizAnswer, Long> {
    fun findByQuizItemId(quizItemId: Long): List<QuizAnswer>
}