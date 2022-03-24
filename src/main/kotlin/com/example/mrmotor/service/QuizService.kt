package com.example.mrmotor.service

import com.example.mrmotor.models.*
import com.example.mrmotor.objects.*

interface QuizService {
    fun listQuizzes(): List<Quiz>
    fun getMyQuizzes(currentUser: User): List<Quiz>

    fun getQuiz(id: Long): Quiz?
    fun createQuiz(currentUser: User, quizDetails: QuizRO): Quiz
    fun updateQuiz(currentUser: User, quizDetails: QuizRO, id: Long): Quiz
    fun deleteQuiz(currentUser: User, id: Long): Boolean

    fun saveQuizResult(currentUser: User, achieved: Int, id: Long): Boolean
    fun deleteQuizResult(currentUser: User, quizId: Long): Boolean
    fun getQuizResults(currentUser: User): List<QuizResult>

    fun getItemsByQuizId(quizId: Long): List<QuizItem>
    fun saveQuizItem(quizItemDetails: QuizItemRO, id: Long): QuizItem
    fun updateQuizItem(quizItemDetails: QuizItemVO, id: Long): QuizItem
    fun deleteQuizItem(id: Long): Boolean

    fun getAnswersByQuizItemId(quizItemId: Long): List<QuizAnswer>
    fun saveQuizAnswer(quizAnswerDetails: QuizAnswerRO, id: Long): QuizAnswer
    fun updateQuizAnswer(quizAnswerDetails: QuizAnswerVO, id: Long): QuizAnswer
    fun deleteQuizAnswer(id: Long): Boolean
}