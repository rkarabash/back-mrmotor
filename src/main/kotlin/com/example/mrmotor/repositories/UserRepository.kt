package com.example.mrmotor.repositories

import com.example.mrmotor.models.User
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями пользователя
 */
interface UserRepository : CrudRepository<User, Long> {
    /***
     * Метод для получение из БД полььзователя по его email
     */
    fun findByEmail(email: String): User?
}