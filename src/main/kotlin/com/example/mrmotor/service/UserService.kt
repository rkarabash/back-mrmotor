package com.example.mrmotor.service

import com.example.mrmotor.models.User

interface UserService {
    fun attemptRegistration(userDetails: User): User
    fun listUsers(currentUser: User): List<User>
    fun retrieveUserData(username: String): User?
    fun retrieveUserData(id: Long): User?
    fun usernameExists(username: String): Boolean
}