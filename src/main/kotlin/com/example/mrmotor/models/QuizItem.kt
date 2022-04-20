package com.example.mrmotor.models

import javax.persistence.*

/***
 * Класс-сущность, представляющий объект вопроса квиза
 */
@Entity
@Table
class QuizItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var question: String = "question",
    @Column(columnDefinition = "TEXT")
    var image: String = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    var quiz: Quiz
) {
    @OneToMany(mappedBy = "quizItem", targetEntity = QuizAnswer::class)
    private var quizAnswers: Collection<QuizAnswer>? = null
}