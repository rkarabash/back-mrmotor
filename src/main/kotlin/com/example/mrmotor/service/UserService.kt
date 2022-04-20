package com.example.mrmotor.service

import com.example.mrmotor.models.User
import com.example.mrmotor.objects.PasswordForgotVO
import com.example.mrmotor.objects.PasswordResetVO

/***
 * Интерфейс, опрделяющий бизнес логику работы с пользователями
 */
interface UserService {
    fun attemptRegistration(userDetails: User): User
    fun listUsers(currentUser: User): List<User>
    fun retrieveUserData(username: String): User?
    fun retrieveUserData(id: Long): User?
    fun usernameExists(username: String): Boolean
    fun updateUserName(currentUser: User, updateDetails: User): User
    fun updateSelfPassword(currentUser: User, updateDetails: User): User
    fun forgotPassword(form: PasswordForgotVO)
    fun resetPassword(form: PasswordResetVO)
}