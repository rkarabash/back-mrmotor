package com.example.mrmotor.components

import com.example.mrmotor.constants.ErrorResponse
import com.example.mrmotor.constants.ResponseConstants
import com.example.mrmotor.exceptions.InvalidUserIdException
import com.example.mrmotor.exceptions.UserNameEmptyException
import com.example.mrmotor.exceptions.UserPasswordEmptyException
import com.example.mrmotor.exceptions.UsernameUnavailableException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserControllerAdvice {
    @ExceptionHandler(UsernameUnavailableException::class)
    fun usernameUnavailable(
        usernameUnavailableException:
        UsernameUnavailableException
    ):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.USERNAME_UNAVAILABLE
                .value, usernameUnavailableException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(InvalidUserIdException::class)
    fun invalidId(invalidUserIdException: InvalidUserIdException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_USER_ID.value,
            invalidUserIdException.message
        )
        return ResponseEntity.badRequest().body(res)
    }

    @ExceptionHandler(UserNameEmptyException::class)
    fun statusEmpty(userNameEmptyException: UserNameEmptyException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.EMPTY_NAME.value,
            userNameEmptyException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }

    @ExceptionHandler(UserPasswordEmptyException::class)
    fun passwordEmpty(userPasswordEmptyException: UserPasswordEmptyException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.EMPTY_PASSWORD.value,
            userPasswordEmptyException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }
}