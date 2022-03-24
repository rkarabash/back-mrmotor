package com.example.mrmotor.exceptions

// USER
class UsernameUnavailableException(override val message: String) :
    RuntimeException()

class InvalidUserIdException(override val message: String) :
    RuntimeException()

class UserDeactivatedException(override val message: String) :
    RuntimeException()

class UserNameEmptyException(override val message: String = "A user' NAME cannot be empty") : RuntimeException()
class UserPasswordEmptyException(override val message: String) : RuntimeException()

// POST
class InvalidPostIdException(override val message: String) :
    RuntimeException()

class PostDataEmptyException(override val message: String) :
    RuntimeException()

class NoLikeFoundException(override val message: String) :
    RuntimeException()

class SealedLikeFoundException(override val message: String) :
    RuntimeException()

// Quiz
class InvalidQuizIdException(override val message: String) :
    RuntimeException()

class QuizResultDataEmptyException(override val message: String) :
    RuntimeException()

class QuizDataEmptyException(override val message: String) :
    RuntimeException()

class FraudAuthorException(override val message: String) :
    RuntimeException()

class NoQuizResultFoundException(override val message: String) :
    RuntimeException()

class SealedQuizResultFoundException(override val message: String) :
    RuntimeException()

class SealedQuizItemFoundException(override val message: String) :
    RuntimeException()

class InvalidQuizItemIdException(override val message: String) :
    RuntimeException()

class QuizItemDataEmptyException(override val message: String) :
    RuntimeException()

class SealedQuizAnswerFoundException(override val message: String) :
    RuntimeException()

class InvalidQuizAnswerIdException(override val message: String) :
    RuntimeException()

class QuizAnswerDataEmptyException(override val message: String) :
    RuntimeException()

