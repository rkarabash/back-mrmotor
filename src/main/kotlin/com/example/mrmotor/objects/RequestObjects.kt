package com.example.mrmotor.objects

class QuizAnswerRO(
    val answer: String,
    val isCorrect: Boolean
)

class QuizItemRO(
    val question: String,
    val image: String,
    val quizAnswers: List<QuizAnswerRO>
)

class QuizRO(
    val title: String,
    val description: String,
    val timer: Int,
    val quizItems: List<QuizItemRO>
)