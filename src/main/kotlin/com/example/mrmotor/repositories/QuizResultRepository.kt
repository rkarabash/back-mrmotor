package com.example.mrmotor.repositories

import com.example.mrmotor.models.QuizResult
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями результата прохождения квиза
 */
interface QuizResultRepository : CrudRepository<QuizResult, Long> {
    /***
     * Метод для получение из БД списка результов прохождения квизов по id пользователя и id квиза
     */
    fun findByUserIdAndQuizId(userId: Long, quizId: Long): QuizResult?

    /***
     * Метод для получение из БД списка результов прохождения квизов по id пользователя
     */
    fun findByUserId(userId: Long): List<QuizResult>

    /***
     * Метод для получение из БД списка результов прохождения квизов по id квиза
     */
    fun findByQuizId(quizId: Long): List<QuizResult>

    /***
     * Метод для получение из БД списка результов прохождения квизов, сортированного по кол-ву набранных баллов
     */
    fun findByQuizIdOrderByAchievedDesc(quizId: Long): List<QuizResult>
}