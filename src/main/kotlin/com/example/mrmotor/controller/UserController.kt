package com.example.mrmotor.controller

import com.example.mrmotor.components.UserAssembler
import com.example.mrmotor.exceptions.InvalidUserIdException
import com.example.mrmotor.models.PasswordResetToken
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.*
import com.example.mrmotor.repositories.PasswordResetTokenRepository
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.service.EmailService
import com.example.mrmotor.service.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping("/users")
class UserController(
    val userService: UserServiceImpl,
    val userAssembler: UserAssembler,
    val userRepository: UserRepository,
    val tokenRepository: PasswordResetTokenRepository,
    val emailService: EmailService
) {
    @PostMapping
    @RequestMapping("/registrations")
    fun create(@Validated @RequestBody userDetails: User):
            ResponseEntity<UserVO> {
        val user = userService.attemptRegistration(userDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    @GetMapping
    @RequestMapping("/{user_id}")
    fun show(@PathVariable("user_id") userId: Long):
            ResponseEntity<UserVO> {
        val user = userService.retrieveUserData(userId)
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    @GetMapping
    @RequestMapping("/details")
    fun echoDetails(request: HttpServletRequest): ResponseEntity<UserVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(userAssembler.toUserVO(user))
    }

    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<UserListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        val users = userService.listUsers(user)
        return ResponseEntity.ok(userAssembler.toUserListVO(users))
    }

    @PutMapping
    fun update(
        @RequestBody updateDetails: User,
        request: HttpServletRequest
    ): ResponseEntity<UserVO> {
        val currentUser = userRepository.findByEmail(request.userPrincipal.name)
        userService.updateUserName(currentUser as User, updateDetails)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }

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

    @PostMapping
    @RequestMapping("/forgot_password")
    fun forgotPassword(@RequestBody form: @Valid PasswordForgotVO, request: HttpServletRequest): ResponseEntity<String>{
        val user = userRepository.findByEmail(form.email)
            ?: throw InvalidUserIdException("User with email ${form.email} does not exist.")
        val token = PasswordResetToken(user = user)
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
        val url = "http://localhost:3000"
        model["resetUrl"] = url + "/reset-password?token=" + token.token
        mail.model = model
        emailService.sendMail(mail)
        return ResponseEntity.ok("Send message with reset password link")
    }

    @PostMapping
    @RequestMapping("/reset_password")
    fun resetPassword(@RequestBody form: @Valid PasswordResetVO): ResponseEntity<String>{
        val token = tokenRepository.findByToken(form.token)
        val user = token!!.user
        userService.updateSelfPassword(user, User(password = form.password))
        tokenRepository.delete(token)
        return ResponseEntity.ok("Password successfully reset")
    }
}


