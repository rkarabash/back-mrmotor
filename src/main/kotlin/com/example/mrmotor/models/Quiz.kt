package com.example.mrmotor.models

import javax.persistence.*

@Entity
@Table
class Quiz(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var title: String = "Title",
    var description: String = "Description",
    var timer: Int = 5,
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    var author: User
) {
    @OneToMany(mappedBy = "quiz", targetEntity = QuizResult::class)
    private var quizResults: Collection<QuizResult>? = null

    @OneToMany(mappedBy = "quiz", targetEntity = QuizItem::class)
    private var quizItems: Collection<QuizItem>? = null
}