package com.example.mrmotor.components

import com.example.mrmotor.models.Quiz
import com.example.mrmotor.models.QuizAnswer
import com.example.mrmotor.models.QuizItem
import com.example.mrmotor.models.QuizResult
import com.example.mrmotor.objects.*
import org.springframework.stereotype.Component

@Component
class QuizAssembler {
    fun toShortQuizVO(quiz: Quiz): ShortQuizVO {
        return ShortQuizVO(
            quiz.id,
            quiz.title,
            quiz.description,
            quiz.timer
        )
    }

    fun toShortQuizListVO(quizzes: List<Quiz>): ShortQuizListVO {
        return ShortQuizListVO(
            quizzes.map { toShortQuizVO(it) }
        )
    }

    fun toQuizResultVO(quizResult: QuizResult): QuizResultVO {
        return QuizResultVO(
            quizResult.id,
            quizResult.achieved,
            quizResult.amount,
            toShortQuizVO(quizResult.quiz)
        )
    }

    fun toQuizAnswerVO(quizAnswer: QuizAnswer): QuizAnswerVO {
        return QuizAnswerVO(
            quizAnswer.id,
            quizAnswer.answer,
            quizAnswer.isCorrect
        )
    }

    fun toQuizItemVO(quizItem: QuizItem, quizAnswers: List<QuizAnswer>): QuizItemVO {
        return QuizItemVO(
            quizItem.id,
            quizItem.question,
            quizItem.image,
            quizAnswers.map { toQuizAnswerVO(it) }
        )
    }

    fun toQuizItems(quizItems: List<QuizItem>, quizAnswers: List<List<QuizAnswer>>): List<QuizItemVO> {
        return quizItems.mapIndexed { index, quizItem -> toQuizItemVO(quizItem, quizAnswers[index]) }
    }

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

    fun toQuizResultListVO(quizResults: List<QuizResult>): QuizResultListVO {
        return QuizResultListVO(quizResults.map { toQuizResultVO(it) })
    }
}