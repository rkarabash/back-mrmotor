package com.example.mrmotor.service

import com.example.mrmotor.exceptions.*
import com.example.mrmotor.models.PasswordResetToken
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.MailVO
import com.example.mrmotor.objects.PasswordForgotVO
import com.example.mrmotor.objects.PasswordResetVO
import com.example.mrmotor.repositories.PasswordResetTokenRepository
import com.example.mrmotor.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import javax.validation.Valid
import kotlin.collections.ArrayList

/***
 * Класс, реализующий бизнес логику работы с пользователями
 */
@Service
class UserServiceImpl(
    val repository: UserRepository,
    val tokenRepository: PasswordResetTokenRepository,
    val emailService: EmailService
) : UserService {
    /***
     * Метод бизнес логики для регистрации пользователя
     */
    @Throws(UsernameUnavailableException::class)
    override fun attemptRegistration(userDetails: User): User {
        if (!usernameExists(userDetails.email)) {
            val user = User()
            user.email = userDetails.email
            user.name = userDetails.name
            user.password = userDetails.password
            user.avatar = userDetails.avatar.ifEmpty { "" }
            repository.save(user)
            obscurePassword(user)
            return user
        }
        throw UsernameUnavailableException("The username with Email ( ${userDetails.email} ) is unavailable.")
    }

    /***
     * Метод бизнес логики для обновления информации о себе пользователем
     */
    @Throws(UserNameEmptyException::class)
    override fun updateUserName(currentUser: User, updateDetails: User): User {
        if (updateDetails.name.isNotEmpty()) {
            currentUser.name = updateDetails.name
            currentUser.avatar = updateDetails.avatar
            repository.save(currentUser)
            return currentUser
        }
        throw UserNameEmptyException()
    }

    /***
     * Метод бизнес логики для смены пароля пользователем
     */
    @Throws(UserPasswordEmptyException::class)
    override fun updateSelfPassword(currentUser: User, updateDetails: User): User {
        if (updateDetails.password.isNotEmpty()) {
            currentUser.password = BCryptPasswordEncoder().encode(updateDetails.password)
            repository.save(currentUser)
            return currentUser
        }
        throw UserPasswordEmptyException("Password must be not empty")
    }

    /***
     * Метод бизнес логики для получения списка пользователей
     */
    override fun listUsers(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }
            .filter { it != currentUser }
    }

    /***
     * Метод бизнес логики для получения информации о пользователе с username
     */
    override fun retrieveUserData(username: String): User? {
        val user = repository.findByEmail(username)
        obscurePassword(user)
        return user
    }

    /***
     * Метод бизнес логики для получения информации о пользователе с id
     */
    @Throws(InvalidUserIdException::class)
    override fun retrieveUserData(id: Long): User {
        val userOptional = repository.findById(id)
        if (userOptional.isPresent) {
            val user = userOptional.get()
            obscurePassword(user)
            return user
        }
        throw InvalidUserIdException("A user with an id of '$id' does not exist.")
    }

    /***
     * Метод бизнес-логики для отправки Email-сообщения с ссылкой на сброс пароля
     */
    @Throws(InvalidFormException::class)
    override fun forgotPassword(form: PasswordForgotVO) {
        if (form.email.isEmpty())
            throw InvalidFormException("Invalid form")
        val user = repository.findByEmail(form.email)
            ?: throw InvalidFormException("User with email ${form.email} does not exist.")
        var token: PasswordResetToken? = tokenRepository.findByUserId(user.id);
        if (token != null) {
            tokenRepository.delete(token);
        }
        token = PasswordResetToken(user = user)
        token.token = UUID.randomUUID().toString()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 15)
        token.expiryDate = calendar.time
        tokenRepository.save(token)
        val mail = MailVO(
            "support@mrmotor.works",
            user.email,
            "Password reset request"
        )
        val model: HashMap<String, Any> = HashMap()
        model["token"] = token
        model["user"] = user
        val url = "http://app.mrmotor.works"
        model["resetUrl"] = url + "/reset-password?token=" + token.token
        mail.model = model
        emailService.sendMail(mail)
    }

    /***
     * Метод бизнес-логики для сброса пароля при утере
     */
    @Throws(InvalidFormException::class)
    override fun resetPassword(form: PasswordResetVO) {
        if (form.token.isEmpty() || form.password.isEmpty())
            throw InvalidFormException("Invalid form")
        val token = tokenRepository.findByToken(form.token) ?: throw InvalidFormException("Invalid token")
        val user = token.user
        updateSelfPassword(user, User(password = form.password))
        tokenRepository.delete(token)
    }

    /***
     * Метод проверки на занятость юзернейма
     */
    override fun usernameExists(username: String): Boolean {
        return repository.findByEmail(username) != null
    }

    /***
     * Метод скрытия пароля
     */
    private fun obscurePassword(user: User?) {
        user?.password = "XXX XXXX XXX"
    }
}
