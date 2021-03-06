package com.example.mrmotor.models

import javax.persistence.*

/***
 * Класс-сущность, представляющий объект ответа на вопрос квиза
 */
@Entity
@Table
class QuizAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var answer: String = "answer",
    var isCorrect: Boolean = false,
    @ManyToOne(optional = false)
    @JoinColumn(name = "quizItem_id", referencedColumnName = "id")
    var quizItem: QuizItem
)