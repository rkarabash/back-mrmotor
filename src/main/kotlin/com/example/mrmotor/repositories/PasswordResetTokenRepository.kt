package com.example.mrmotor.repositories

import com.example.mrmotor.models.PasswordResetToken
import org.springframework.data.repository.CrudRepository

/***
 * Интерфейс, определенный для доступа к таблице с сущностями токена сброса пароля
 */
interface PasswordResetTokenRepository : CrudRepository<PasswordResetToken, Long> {
    /***
     * Метод для получение из БД токена сброса пароля по токену
     */
    fun findByToken(token: String): PasswordResetToken?

    /***
     * Метод для получение из БД токена сброса пароля по id пользователя
     */
    fun findByUserId(id: Long): PasswordResetToken?
}