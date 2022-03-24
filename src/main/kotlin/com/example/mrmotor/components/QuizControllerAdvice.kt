package com.example.mrmotor.components

import com.example.mrmotor.constants.ErrorResponse
import com.example.mrmotor.constants.ResponseConstants
import com.example.mrmotor.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.RuntimeException

@ControllerAdvice
class QuizControllerAdvice {
    @ExceptionHandler(
        InvalidQuizIdException::class,
        InvalidQuizItemIdException::class,
        InvalidQuizAnswerIdException::class
    )
    fun invalidId(runtimeException: RuntimeException)
            : ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_QUIZ_ID.value,
            runtimeException.message as String
        )
        return ResponseEntity.badRequest().body(res)
    }

    @ExceptionHandler(FraudAuthorException::class)
    fun fraudAuthor(runtimeException: RuntimeException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.FRAUD_AUTHOR.value,
            runtimeException.message as String
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res)
    }

    @ExceptionHandler(
        QuizDataEmptyException::class,
        QuizItemDataEmptyException::class,
        QuizAnswerDataEmptyException::class,
        QuizResultDataEmptyException::class
    )
    fun emptyData(runtimeException: RuntimeException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.INVALID_QUIZ_DATA.value,
            runtimeException.message as String
        )
        return ResponseEntity.badRequest().body(res)
    }

    @ExceptionHandler(
        SealedQuizItemFoundException::class,
        SealedQuizAnswerFoundException::class,
        SealedQuizResultFoundException::class
    )
    fun sealedItem(runtimeException: RuntimeException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.SEALED_QUIZ_ITEMS.value,
            runtimeException.message as String
        )
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res)
    }

    @ExceptionHandler(NoQuizResultFoundException::class)
    fun noQuizResult(runtimeException: RuntimeException):
            ResponseEntity<ErrorResponse> {
        val res = ErrorResponse(
            ResponseConstants.NO_QUIZ_RESULT_FOUND.value,
            runtimeException.message as String
        )
        return ResponseEntity.badRequest().body(res)
    }
}