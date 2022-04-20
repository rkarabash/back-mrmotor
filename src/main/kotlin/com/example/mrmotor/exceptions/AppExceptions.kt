package com.example.mrmotor.exceptions

// USER
/***
 * Класс-исключение, вызывающийся при отсутствие запрошенного пользователя
 */
class UsernameUnavailableException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидного id пользователя
 */
class InvalidUserIdException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных по сбросу пароля
 */
class InvalidFormException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при отсутствие запрошенного пользователя
 */
class UserDeactivatedException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при отсутствие имени пользователя при редактировании пользователя
 */
class UserNameEmptyException(override val message: String = "A user' NAME cannot be empty") : RuntimeException()

/***
 * Класс-исключение, вызывающийся при невалидных данных при смене пароля пользователя
 */
class UserPasswordEmptyException(override val message: String) : RuntimeException()

// POST
/***
 * Класс-исключение, вызывающийся при получение невалидного id информационного поста
 */
class InvalidPostIdException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных информационного поста
 */
class PostDataEmptyException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидного id пометки "Нравится"
 */
class NoLikeFoundException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при обнаружение непривязанной к посту пометки "Нравится"
 */
class SealedLikeFoundException(override val message: String) :
    RuntimeException()

// Quiz
/***
 * Класс-исключение, вызывающийся при получение невалидного id квиза
 */
class InvalidQuizIdException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных результата прохождения квиза
 */
class QuizResultDataEmptyException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных квиза
 */
class QuizDataEmptyException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при попытке пользователя удалить или редактировать квиз другого пользователя
 */
class FraudAuthorException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидного id результата прохождения квиза
 */
class NoQuizResultFoundException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при обнаружение непривязанного к квизу результата прохождения квиза
 */
class SealedQuizResultFoundException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при обнаружение непривязанного к квизу вопроса
 */
class SealedQuizItemFoundException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидного id вопроса квиза
 */
class InvalidQuizItemIdException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных вопроса квиза
 */
class QuizItemDataEmptyException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при обнаружение непривязанного к вопросу квиза ответа
 */
class SealedQuizAnswerFoundException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидного id ответа на вопрос квиза
 */
class InvalidQuizAnswerIdException(override val message: String) :
    RuntimeException()

/***
 * Класс-исключение, вызывающийся при получение невалидных данных ответа на вопрос квиза
 */
class QuizAnswerDataEmptyException(override val message: String) :
    RuntimeException()

