package com.example.mrmotor.components

import com.example.mrmotor.constants.ErrorResponse
import com.example.mrmotor.constants.ResponseConstants
import com.example.mrmotor.exceptions.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


/***
 * Класс-обработчик невалидных запросов на бизнес логику пользователя
 */
@ControllerAdvice
class UserControllerAdvice {
    /***
     * Метод, обрабатывающий исключение, вызванного при отсутствие запрошенного пользователя
     */
    @ExceptionHandler(UsernameUnavailableException::class)
    fun usernameUnavailable(
        usernameUnavailableException:
        UsernameUnavailableException
    ): ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.USERNAME_UNAVAILABLE
                .value, usernameUnavailableException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при получение невалидного id пользователя
     */
    @ExceptionHandler(InvalidUserIdException::class)
    fun invalidId(invalidUserIdException: InvalidUserIdException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_USER_ID.value,
            invalidUserIdException.message
        )
        return ResponseEntity.badRequest().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при получение невалидных данных по сбросу пароля
     */
    @ExceptionHandler(InvalidFormException::class)
    fun invalidForm(invalidFormException: InvalidFormException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_USER_ID.value,
            invalidFormException.message
        )
        return ResponseEntity.badRequest().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при отсутствие имени пользователя при редактировании пользователя
     */
    @ExceptionHandler(UserNameEmptyException::class)
    fun statusEmpty(userNameEmptyException: UserNameEmptyException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.EMPTY_NAME.value,
            userNameEmptyException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при невалидных данных при смене пароля пользователя
     */
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