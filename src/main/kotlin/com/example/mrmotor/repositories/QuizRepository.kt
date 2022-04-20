package com.example.mrmotor.repositories

import com.example.mrmotor.models.Quiz
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/***
 * Интерфейс, определенный для доступа к таблице с сущностями квиза
 */
interface QuizRepository : CrudRepository<Quiz, Long> {
    /***
     * Метод для получение из БД списка квизов автора по id автора
     */
    fun findByAuthorIdOrderById(authorId: Long): List<Quiz>

    /***
     * Метод для получение из БД списка квизов
     */
    fun findAllByOrderByIdDesc(): List<Quiz>

    /***
     * Метод для получение из БД списка квизов с ограничением на кол-во
     */
    @Query(value = "select * from quiz q order by q.id desc offset :offset limit :limit", nativeQuery = true)
    fun findAllWithLimit(@Param("offset") offset: Int, @Param("limit") limit: Int): List<Quiz>

}