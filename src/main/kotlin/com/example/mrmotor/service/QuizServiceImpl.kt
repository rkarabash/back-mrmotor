package com.example.mrmotor.service

import com.example.mrmotor.exceptions.*
import com.example.mrmotor.models.*
import com.example.mrmotor.objects.*
import com.example.mrmotor.repositories.QuizAnswerRepository
import com.example.mrmotor.repositories.QuizItemRepository
import com.example.mrmotor.repositories.QuizRepository
import com.example.mrmotor.repositories.QuizResultRepository
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/***
 * Класс, реализующий бизнес логику работы с квизами
 */
@Service
class QuizServiceImpl(
    val quizRepository: QuizRepository,
    val quizAnswerRepository: QuizAnswerRepository,
    val quizItemRepository: QuizItemRepository,
    val quizResultRepository: QuizResultRepository
) : QuizService {
    /***
     * Метод бизнес логики для получения списка квизов
     */
    override fun listQuizzes(): List<Quiz> {
        return quizRepository.findAllByOrderByIdDesc()
    }

    /***
     * Метод бизнес логики для получения списка квизов с ограничением на кол-во
     */
    override fun listQuizzesWithLimit(offset: Int, limit: Int): List<Quiz> {
        return quizRepository.findAllWithLimit(offset, limit)
    }

    /***
     * Метод бизнес логики для получения списка собственных квизов
     */
    override fun getMyQuizzes(currentUser: User): List<Quiz> {
        return quizRepository.findByAuthorIdOrderById(currentUser.id)
    }

    /***
     * Метод бизнес логики для получения квиза по его id
     */
    @Throws(InvalidQuizIdException::class)
    override fun getQuiz(id: Long): Quiz? {
        val quizOptional = quizRepository.findById(id)
        if (quizOptional.isPresent) {
            return quizOptional.get()
        }
        throw InvalidQuizIdException("Quiz with id of '$id' does not exist.")
    }

    /***
     * Метод бизнес логики для создания квиза
     */
    @Throws(QuizDataEmptyException::class)
    override fun createQuiz(currentUser: User, quizDetails: QuizRO): Quiz {
        if (quizDetails.title.isNotEmpty()) {
            var quiz = Quiz(author = currentUser)
            quiz.title = quizDetails.title
            quiz.description = quizDetails.description
            quiz.timer = quizDetails.timer
            quiz = quizRepository.save(quiz)
            quizDetails.quizItems.forEach { saveQuizItem(it, quiz.id) }
            return quiz
        }
        throw QuizDataEmptyException("Title field is required.")
    }

    /***
     * Метод бизнес логики для редактирования квиза по его id
     */
    @Throws(
        InvalidQuizIdException::class,
        QuizDataEmptyException::class,
        FraudAuthorException::class,
        SealedQuizItemFoundException::class
    )
    override fun updateQuiz(currentUser: User, quizDetails: QuizRO, id: Long): Quiz {
        val quizOptional = quizRepository.findById(id)
        if (quizOptional.isPresent && quizOptional.get().author.id != currentUser.id)
            throw FraudAuthorException("You have not permissions to edit this quiz")
        if (quizOptional.isPresent) {
            if (quizDetails.title.isEmpty())
                throw QuizDataEmptyException("Title field is required.")
            quizItemRepository.findByQuizId(id).forEach { deleteQuizItem(it.id) }
            if (quizItemRepository.findByQuizId(id).isNotEmpty())
                throw SealedQuizItemFoundException("Updating quiz with deleting quiz items is required")
            var quiz = quizOptional.get()
            quiz.title = quizDetails.title
            quiz.description = quizDetails.description
            quiz.timer = quizDetails.timer
            quiz = quizRepository.save(quiz)
            quizDetails.quizItems.forEach { saveQuizItem(it, quiz.id) }
            return quiz
        }
        throw InvalidQuizIdException("Quiz with id of '$id' does not exist.")
    }

    /***
     * Метод бизнес логики для удаления квиза по его id
     */
    @Throws(
        SealedQuizResultFoundException::class,
        InvalidQuizIdException::class,
        SealedQuizItemFoundException::class,
        FraudAuthorException::class
    )
    override fun deleteQuiz(currentUser: User, id: Long): Boolean {
        val quizOptional = quizRepository.findById(id)
        if (quizOptional.isPresent && quizOptional.get().author.id != currentUser.id)
            throw FraudAuthorException("You have not permissions to edit this quiz")
        if (quizOptional.isPresent) {
            val results = quizResultRepository.findByQuizId(id)
            quizResultRepository.deleteAll(results)
            if (quizResultRepository.findByQuizId(id).isNotEmpty())
                throw SealedQuizResultFoundException("Deleting results with quiz is required")
            quizItemRepository.findByQuizId(id).forEach { deleteQuizItem(it.id) }
            if (quizItemRepository.findByQuizId(id).isNotEmpty())
                throw SealedQuizItemFoundException("Deleting quiz items with quiz is required")
            quizRepository.deleteById(id)
            return true
        }
        throw InvalidQuizIdException("Quiz with id of '$id' does not exist.")
    }

    /***
     * Метод бизнес-логики для записи результата прохождения квиза
     */
    @Throws(InvalidQuizIdException::class, QuizResultDataEmptyException::class)
    override fun saveQuizResult(currentUser: User, achieved: Int, id: Long): Boolean {
        val quizOptional = quizRepository.findById(id)
        if (quizOptional.isPresent) {
            val numItems = quizItemRepository.findByQuizId(quizOptional.get().id).size
            if (achieved < 0 || achieved > numItems)
                throw QuizResultDataEmptyException("Achieved field is required, must be positive and less or equal to number of quiz items!")
            val result = quizResultRepository.findByUserIdAndQuizId(currentUser.id, quizOptional.get().id)
            if (result != null)
                quizResultRepository.deleteById(result.id)
            val quiz = QuizResult(
                achieved,
                numItems,
                currentUser,
                quizOptional.get()
            )
            quizResultRepository.save(quiz)
            return true
        }
        throw InvalidQuizIdException("Quiz with id of '${id}' does not exist.")
    }

    /***
     * Метод бизнес-логики для удаления результата прохождения квиза
     */
    @Throws(NoQuizResultFoundException::class)
    override fun deleteQuizResult(currentUser: User, quizId: Long): Boolean {
        val quizResult = quizResultRepository.findByUserIdAndQuizId(currentUser.id, quizId)
        if (quizResult != null) {
            quizResultRepository.deleteById(quizResult.id)
            return true
        }
        throw NoQuizResultFoundException("The user '${currentUser.name}' has not quiz result to quiz with id '$quizId'")
    }

    /***
     * Метод бизнес логики для получения списка результатов прохождения квизов
     */
    override fun getQuizResults(currentUser: User): List<QuizResult> {
        return quizResultRepository.findByUserId(currentUser.id)
    }

    /***
     * Метод бизнес логики для получения рейтинга пользователей прошедших квиз с ID
     */
    override fun getTop(quizId: Long): List<QuizResult> {
        return quizResultRepository.findByQuizIdOrderByAchievedDesc(quizId)
    }

    /***
     * Метод бизнес логики для получения списка вопросов квиза по его id квиза
     */
    @Throws(InvalidQuizIdException::class)
    override fun getItemsByQuizId(quizId: Long): List<QuizItem> {
        val quizOptional = quizRepository.findById(quizId)
        if (quizOptional.isPresent) {
            return quizItemRepository.findByQuizId(quizId)
        }
        throw InvalidQuizIdException("Quiz with id of '$quizId' does not exist.")
    }

    /***
     * Метод бизнес логики для создания вопроса квиза
     */
    @Throws(InvalidQuizIdException::class, QuizItemDataEmptyException::class)
    override fun saveQuizItem(quizItemDetails: QuizItemRO, id: Long): QuizItem {
        val quizOptional = quizRepository.findById(id)
        if (quizOptional.isPresent) {
            if (quizItemDetails.question.isEmpty())
                throw QuizItemDataEmptyException("Question field is required")
            var quizItem = QuizItem(quiz = quizOptional.get())
            quizItem.question = quizItemDetails.question
            quizItem.image = quizItemDetails.image
            quizItem = quizItemRepository.save(quizItem)
            quizItemDetails.quizAnswers.forEach { saveQuizAnswer(it, quizItem.id) }
            return quizItem
        }
        throw InvalidQuizIdException("Quiz with id of '${id}' does not exist.")
    }

    /***
     * Метод бизнес логики для редактирования вопроса квиза
     */
    @Throws(InvalidQuizItemIdException::class, QuizItemDataEmptyException::class)
    override fun updateQuizItem(quizItemDetails: QuizItemVO, id: Long): QuizItem {
        val quizItemOptional = quizItemRepository.findById(id)
        if (quizItemOptional.isPresent) {
            if (quizItemDetails.question.isEmpty())
                throw QuizItemDataEmptyException("Question field is required")
            val quizItem = quizItemOptional.get()
            quizItem.question = quizItemDetails.question
            quizItem.image = quizItemDetails.image
            quizItemRepository.save(quizItem)
            return quizItem
        }
        throw InvalidQuizItemIdException("Quiz item with id = $id does not exist")
    }

    /***
     * Метод бизнес логики для удаления вопроса квиза
     */
    @Throws(InvalidQuizItemIdException::class, SealedQuizAnswerFoundException::class)
    override fun deleteQuizItem(id: Long): Boolean {
        val quizItemOptional = quizItemRepository.findById(id)
        if (quizItemOptional.isPresent) {
            quizAnswerRepository.deleteAll(quizAnswerRepository.findByQuizItemId(quizItemOptional.get().id))
            if (quizAnswerRepository.findByQuizItemId(quizItemOptional.get().id).isNotEmpty())
                throw SealedQuizAnswerFoundException("Deleting quiz answers with quiz items is required")
            quizItemRepository.delete(quizItemOptional.get())
            return true
        }
        throw InvalidQuizItemIdException("Quiz item with id = $id does not exist")
    }

    /***
     * Метод бизнес логики для получения списка ответов на вопрос квиза по его id вопроса
     */
    @Throws(InvalidQuizItemIdException::class)
    override fun getAnswersByQuizItemId(quizItemId: Long): List<QuizAnswer> {
        val quizItemOptional = quizItemRepository.findById(quizItemId)
        if (quizItemOptional.isPresent) {
            return quizAnswerRepository.findByQuizItemId(quizItemId)
        }
        throw InvalidQuizItemIdException("Quiz item with id = $quizItemId does not exist")
    }

    /***
     * Метод бизнес логики для создания ответа на вопрос квиза
     */
    @Throws(InvalidQuizItemIdException::class, QuizAnswerDataEmptyException::class)
    override fun saveQuizAnswer(quizAnswerDetails: QuizAnswerRO, id: Long): QuizAnswer {
        val quizItemOptional = quizItemRepository.findById(id)
        if (quizItemOptional.isPresent) {
            if (quizAnswerDetails.answer.isEmpty())
                throw QuizAnswerDataEmptyException("Answer field is required")
            val quizAnswer = QuizAnswer(quizItem = quizItemOptional.get())
            quizAnswer.answer = quizAnswerDetails.answer
            quizAnswer.isCorrect = quizAnswerDetails.isCorrect
            quizAnswerRepository.save(quizAnswer)
            return quizAnswer
        }
        throw InvalidQuizItemIdException("Quiz item with id of '${id}' does not exist.")
    }

    /***
     * Метод бизнес логики для редактирования ответа на вопрос квиза
     */
    @Throws(InvalidQuizAnswerIdException::class, QuizAnswerDataEmptyException::class)
    override fun updateQuizAnswer(quizAnswerDetails: QuizAnswerVO, id: Long): QuizAnswer {
        val quizAnswerOptional = quizAnswerRepository.findById(id)
        if (quizAnswerOptional.isPresent) {
            if (quizAnswerDetails.answer.isEmpty())
                throw QuizAnswerDataEmptyException("Answer field is required")
            val quizAnswer = quizAnswerOptional.get()
            quizAnswer.answer = quizAnswerDetails.answer
            quizAnswer.isCorrect = quizAnswerDetails.isCorrect
            quizAnswerRepository.save(quizAnswer)
            return quizAnswer
        }
        throw InvalidQuizAnswerIdException("Quiz answer with id = $id does not exist")
    }

    /***
     * Метод бизнес логики для удаления ответа на вопрос квиза
     */
    @Throws(InvalidQuizAnswerIdException::class)
    override fun deleteQuizAnswer(id: Long): Boolean {
        val quizAnswerOptional = quizAnswerRepository.findById(id)
        if (quizAnswerOptional.isPresent) {
            quizAnswerRepository.deleteById(id)
            return true
        }
        throw InvalidQuizAnswerIdException("Quiz answer with id = $id does not exist")
    }
}