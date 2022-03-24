package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizItem
import org.springframework.data.repository.CrudRepository

interface QuizItemRepository: CrudRepository<QuizItem, Long> {
    fun findByQuizId(quizId: Long): List<QuizItem>
}