package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizItem
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями вопроса квиза
 */
interface QuizItemRepository : CrudRepository<QuizItem, Long> {
    /***
     * Метод для получение из БД списка вопросов квиза по id квиза
     */
    fun findByQuizId(quizId: Long): List<QuizItem>
}