package com.example.mrmotor.components

import com.example.mrmotor.constants.ErrorResponse
import com.example.mrmotor.constants.ResponseConstants
import com.example.mrmotor.exceptions.InvalidPostIdException
import com.example.mrmotor.exceptions.NoLikeFoundException
import com.example.mrmotor.exceptions.PostDataEmptyException
import com.example.mrmotor.exceptions.SealedLikeFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/***
 * Класс-обработчик невалидных запросов на бизнес логику информационных постов
 */
@ControllerAdvice
class PostControllerAdvice {
    /***
     * Метод, обрабатывающий исключение, вызванного при подаче неверного id информационного поста
     */
    @ExceptionHandler(InvalidPostIdException::class)
    fun invalidId(invalidPostIdException: InvalidPostIdException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_POST_ID.value,
            invalidPostIdException.message
        )
        return ResponseEntity.badRequest().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при подаче невалидных данных информационного поста
     */
    @ExceptionHandler(PostDataEmptyException::class)
    fun emptyData(postDataEmptyException: PostDataEmptyException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.POST_DATA_EMPTY.value,
            postDataEmptyException.message
        )
        return ResponseEntity.unprocessableEntity().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при получение невалидного id пометки "Нравится"
     */
    @ExceptionHandler(NoLikeFoundException::class)
    fun noLike(noLikeFoundException: NoLikeFoundException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.NO_LIKE_FOUND.value,
            noLikeFoundException.message
        )
        return ResponseEntity.badRequest().body(res)
    }

    /***
     * Метод, обрабатывающий исключение, вызванного при обнаружение непривязанной к посту пометки "Нравится"
     */
    @ExceptionHandler(SealedLikeFoundException::class)
    fun sealedLike(sealedLikeFoundException: SealedLikeFoundException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.SEALED_LIKE_FOUND.value,
            sealedLikeFoundException.message
        )
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res)
    }
}