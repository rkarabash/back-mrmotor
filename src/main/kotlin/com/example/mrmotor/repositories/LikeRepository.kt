package com.example.mrmotor.repositories

import com.example.mrmotor.models.Like
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями пометки "Нравится"
 */
interface LikeRepository : CrudRepository<Like, Long> {
    /***
     * Метод для получение из БД списка пометок нравится по id информационного поста
     */
    fun findByPostId(id: Long): List<Like>

    /***
     * Метод для получение из БД списка пометок нравится по id пользователя
     */
    fun findByUserId(id: Long): List<Like>

    /***
     * Метод для получение из БД списка пометок нравится по id информационного поста и по id пользователя
     */
    fun findByPostIdAndUserId(postId: Long, userId: Long): Like?
}