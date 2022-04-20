package com.example.mrmotor.controller

import com.example.mrmotor.exceptions.InvalidUserIdException
import com.example.mrmotor.objects.TokenVO
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.security.AccountCredentials
import com.example.mrmotor.service.TokenAuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

/***
 * Класс-контроллер, отвечающий за обработку запросов, связанных с аутентификацией пользователя
 */
@RestController
@CrossOrigin
class LoginController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository
) {
    /***
     * Метод обработки конечной точки API для авторизации пользователя
     */
    @PostMapping("/ulogin")
    fun login(@RequestBody credentials: AccountCredentials): ResponseEntity<TokenVO> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentials.username,
                credentials.password,
                emptyList()
            )
        )
        if (userRepository.findByEmail(credentials.username) != null)
            return ResponseEntity.ok(TokenVO("Bearer ${TokenAuthenticationService.getJWT(credentials.username)}"))

        throw InvalidUserIdException("User with email = ${credentials.username} does not exist.")
    }
}