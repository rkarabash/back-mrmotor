package com.example.mrmotor.objects

import com.example.mrmotor.models.Post
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class UserVO(
    val id: Long,
    val email: String,
    val name: String,
    val avatar: String
)

data class TokenVO(
    val token: String
)

data class UserListVO(
    val users: List<UserVO>
)

data class PostListVO(
    val posts: List<Post>
)

data class ShortQuizListVO(
    val quizzes: List<ShortQuizVO>
)

data class ShortQuizVO(
    val id: Long,
    val title: String,
    val description: String,
    val timer: Int,
)

data class QuizVO(
    val id: Long,
    val title: String,
    val description: String,
    val timer: Int,
    val author: UserVO,
    val quizItems: List<QuizItemVO>
)

data class QuizItemVO(
    val id: Long,
    val question: String,
    val image: String,
    val quizAnswers: List<QuizAnswerVO>
)

data class QuizAnswerVO(
    val id: Long,
    val answer: String,
    val isCorrect: Boolean
)

data class QuizResultVO(
    val id: Long,
    val achieved: Int,
    val amount: Int,
    val quiz: ShortQuizVO
)

data class QuizResultListVO(
    val quizResults: List<QuizResultVO>
)

data class PasswordForgotVO(
    @Email
    @NotEmpty
    val email: String = "abs@mail.com"
)

data class PasswordResetVO(
    @NotEmpty
    val password: String = "123",
    @NotEmpty
    val confirmPassword: String = "123",
    @NotEmpty
    val token: String = "123"
)

data class MailVO(
    val from: String,
    val to: String,
    val subject: String,
    var model: Map<String, Any> = HashMap()
)