package com.example.mrmotor.components

import com.example.mrmotor.models.Quiz
import com.example.mrmotor.models.QuizAnswer
import com.example.mrmotor.models.QuizItem
import com.example.mrmotor.models.QuizResult
import com.example.mrmotor.objects.*
import org.springframework.stereotype.Component

/***
 * Класс, отвечающий за приведение объектов квизов в объекты-значения
 */
@Component
class QuizAssembler {
    /***
     * Метод, конвертирующий в объект-значение ShortQuizVO
     */
    fun toShortQuizVO(quiz: Quiz): ShortQuizVO {
        return ShortQuizVO(
            quiz.id,
            quiz.title,
            quiz.description,
            quiz.timer
        )
    }

    /***
     * Метод, конвертирующий в объект-значение ShortListQuizVO
     */
    fun toShortQuizListVO(quizzes: List<Quiz>): ShortQuizListVO {
        return ShortQuizListVO(
            quizzes.map { toShortQuizVO(it) }
        )
    }

    /***
     * Метод, конвертирующий в объект-значение QuizResultVO
     */
    fun toQuizResultVO(quizResult: QuizResult): QuizResultVO {
        return QuizResultVO(
            quizResult.id,
            quizResult.achieved,
            quizResult.amount,
            toShortQuizVO(quizResult.quiz)
        )
    }

    /***
     * Метод, конвертирующий в объект-значение TopResultVO
     */
    fun toTopResultVO(quizResult: QuizResult): TopResultVO {
        return TopResultVO(quizResult.id, quizResult.achieved, quizResult.user.id, quizResult.user.name)
    }

    /***
     * Метод, конвертирующий в объект-значение TopVO
     */
    fun toTopVO(quizResults: List<QuizResult>): TopVO {
        return TopVO(
            quizResults.map { toTopResultVO(it) },
            if (quizResults.isNotEmpty()) quizResults[0].amount else 0,
            if (quizResults.isNotEmpty()) toShortQuizVO(quizResults[0].quiz) else ShortQuizVO(
                0,
                "NO Quiz",
                "No Quiz",
                0
            )
        )
    }

    /***
     * Метод, конвертирующий в объект-значение QuizAnswerVO
     */
    fun toQuizAnswerVO(quizAnswer: QuizAnswer): QuizAnswerVO {
        return QuizAnswerVO(
            quizAnswer.id,
            quizAnswer.answer,
            quizAnswer.isCorrect
        )
    }

    /***
     * Метод, конвертирующий в объект-значение QuizItemVO
     */
    fun toQuizItemVO(quizItem: QuizItem, quizAnswers: List<QuizAnswer>): QuizItemVO {
        return QuizItemVO(
            quizItem.id,
            quizItem.question,
            quizItem.image,
            quizAnswers.map { toQuizAnswerVO(it) }
        )
    }

    /***
     * Метод, конвертирующий в список объектов-значений QuizItemVO
     */
    fun toQuizItems(quizItems: List<QuizItem>, quizAnswers: List<List<QuizAnswer>>): List<QuizItemVO> {
        return quizItems.mapIndexed { index, quizItem -> toQuizItemVO(quizItem, quizAnswers[index]) }
    }

    /***
     * Метод, конвертирующий в объект-значение QuizVO
     */
    fun toQuizVO(quiz: Quiz, quizItems: List<QuizItem>, quizAnswers: List<List<QuizAnswer>>): QuizVO {
        return QuizVO(
            quiz.id,
            quiz.title,
            quiz.description,
            quiz.timer,
            UserAssembler().toUserVO(quiz.author),
            toQuizItems(quizItems, quizAnswers)
        )
    }

    /***
     * Метод, конвертирующий в объект-значение QuizResultListVO
     */
    fun toQuizResultListVO(quizResults: List<QuizResult>): QuizResultListVO {
        return QuizResultListVO(quizResults.map { toQuizResultVO(it) })
    }
}