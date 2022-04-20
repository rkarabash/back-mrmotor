package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizAnswer
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями ответа на вопрос квиза
 */
interface QuizAnswerRepository : CrudRepository<QuizAnswer, Long> {
    /***
     * Метод для получение из БД списка ответов на вопрос квиза по id вопроса
     */
    fun findByQuizItemId(quizItemId: Long): List<QuizAnswer>
}