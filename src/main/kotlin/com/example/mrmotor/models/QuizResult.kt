package com.example.mrmotor.models

import javax.persistence.*

/***
 * Класс-сущность, представляющий объект результата прохождения квиза
 */
@Entity
@Table
class QuizResult(
    var achieved: Int = -1,
    var amount: Int = 0,
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User,
    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    var quiz: Quiz,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
)