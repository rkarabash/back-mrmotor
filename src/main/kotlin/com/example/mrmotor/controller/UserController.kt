package com.example.mrmotor.controller

import com.example.mrmotor.components.UserAssembler
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.*
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/***
 * Класс-контроллер, отвечающий за обработку запросов, связанных с работой бизнес логики пользователя
 */
@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController(
    val userService: UserServiceImpl,
    val userAssembler: UserAssembler,
    val userRepository: UserRepository,
) {
    /***
     * Метод обработки конечной точки API для регистрации пользователя
     */
    @PostMapping
    @RequestMapping("/registrations")
    fun create(@Validated @RequestBody userDetails: User):
            ResponseEntity<UserVO> {
        val user = userService.attemptRegistration(userDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    /***
     * Метод обработки конечной точки API для получения информации о пользователе с id
     */
    @GetMapping
    @RequestMapping("/{user_id}")
    fun show(@PathVariable("user_id") userId: Long):
            ResponseEntity<UserVO> {
        val user = userService.retrieveUserData(userId)
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    /***
     * Метод обработки конечной точки API для получения пользователем информации о себе
     */
    @GetMapping
    @RequestMapping("/details")
    fun echoDetails(request: HttpServletRequest): ResponseEntity<UserVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    /***
     * Метод обработки конечной точки API для получения списка пользователей
     */
    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<UserListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        val users = userService.listUsers(user)
        return ResponseEntity.ok(userAssembler.toUserListVO(users))
    }

    /***
     * Метод обработки конечной точки API для смены пароля пользователем
     */
    @PutMapping
    @CrossOrigin
    @RequestMapping("/change_password")
    fun changePassword(
        @RequestBody updateDetails: User,
        request: HttpServletRequest
    ): ResponseEntity<UserVO> {
        val currentUser = userRepository.findByEmail(request.userPrincipal.name)
        userService.updateSelfPassword(currentUser as User, updateDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }

    /***
     * Метод обработки конечной точки API для обновления информации о себе пользователем
     */
    @PutMapping
    fun update(
        @RequestBody updateDetails: User,
        request: HttpServletRequest
    ): ResponseEntity<UserVO> {
        val currentUser = userRepository.findByEmail(request.userPrincipal.name)
        userService.updateUserName(currentUser as User, updateDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }

    /***
     * Метод обработки конечной точки API для отправки Email-сообщения с ссылкой на сброс пароля
     */
    @PostMapping
    @RequestMapping("/forgot_password")
    fun forgotPassword(
        @RequestBody form: @Valid PasswordForgotVO,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        userService.forgotPassword(form);
        return ResponseEntity.ok("Send message with reset password link")
    }

    /***
     * Метод обработки конечной точки API для сброса пароля при утере
     */
    @PostMapping
    @RequestMapping("/reset_password")
    fun resetPassword(@RequestBody form: @Valid PasswordResetVO): ResponseEntity<String> {
        userService.resetPassword(form);
        return ResponseEntity.ok("Password successfully reset")
    }
}


