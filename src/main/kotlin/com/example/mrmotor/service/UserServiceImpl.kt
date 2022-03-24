package com.example.mrmotor.service

import com.example.mrmotor.exceptions.InvalidUserIdException
import com.example.mrmotor.exceptions.UserNameEmptyException
import com.example.mrmotor.exceptions.UserPasswordEmptyException
import com.example.mrmotor.exceptions.UsernameUnavailableException
import com.example.mrmotor.models.User
import com.example.mrmotor.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val repository: UserRepository) : UserService {
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
    @Throws(UserNameEmptyException::class)
    fun updateUserName(currentUser: User, updateDetails: User): User {
        if (updateDetails.name.isNotEmpty()) {
            currentUser.name = updateDetails.name
            currentUser.avatar = updateDetails.avatar
            repository.save(currentUser)
            return currentUser
        }
        throw UserNameEmptyException()
    }
    @Throws(UserPasswordEmptyException::class)
    fun updateSelfPassword(currentUser: User, updateDetails: User): User {
        if (updateDetails.password.isNotEmpty()) {
            currentUser.password = BCryptPasswordEncoder().encode(updateDetails.password)
            repository.save(currentUser)
            return currentUser
        }
        throw UserPasswordEmptyException("Password must be not empty")
    }
    override fun listUsers(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList()) { it }
            .filter{ it != currentUser }
    }
    override fun retrieveUserData(username: String): User? {
        val user = repository.findByEmail(username)
        obscurePassword(user)
        return user
    }
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
    override fun usernameExists(username: String): Boolean {
        return repository.findByEmail(username) != null
    }
    private fun obscurePassword(user: User?) {
        user?.password = "XXX XXXX XXX"
    }
}
