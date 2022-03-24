package com.example.mrmotor.models

import com.example.mrmotor.listeners.UserListener
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "`user`")
@EntityListeners(UserListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    @Column(unique = true)
    @Size(min = 5)
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    var email: String = "",
    @Size(min = 60, max = 60)
    var password: String = "",
    var name: String = "User",
    @Column(columnDefinition = "TEXT")
    var avatar: String = ""
){
    @OneToMany(mappedBy = "user", targetEntity = Like::class)
    private var likes: Collection<Like>? = null

    @OneToMany(mappedBy = "author", targetEntity = Quiz::class)
    private var quizzes: Collection<Quiz>? = null

    @OneToMany(mappedBy = "user", targetEntity = QuizResult::class)
    private var quizResults: Collection<QuizResult>? = null
}