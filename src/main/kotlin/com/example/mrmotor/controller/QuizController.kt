package com.example.mrmotor.controller

import com.example.mrmotor.components.QuizAssembler
import com.example.mrmotor.models.Quiz
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.*
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.service.QuizServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/***
 * Класс-контроллер, отвечающий за обработку запросов, связанных с работой бизнес логики квизов
 */
@RestController
@CrossOrigin
@RequestMapping("/quiz")
class QuizController(
    val quizService: QuizServiceImpl,
    val quizAssembler: QuizAssembler,
    val userRepository: UserRepository
) {
    /***
     * Метод обработки конечной точки API для получения списка квизов
     */
    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<ShortQuizListVO> {
        return ResponseEntity.ok(quizAssembler.toShortQuizListVO(quizService.listQuizzes()))
    }

    /***
     * Метод обработки конечной точки API для получения списка квизов с ограничением на кол-во
     */
    @RequestMapping("/limit", method = [RequestMethod.GET])
    fun indexWithLimit(
        @RequestParam("offset") offset: Int,
        @RequestParam("limit") limit: Int,
        request: HttpServletRequest
    ): ResponseEntity<ShortQuizListVO> {
        return ResponseEntity.ok(quizAssembler.toShortQuizListVO(quizService.listQuizzesWithLimit(offset, limit)))
    }

    /***
     * Метод обработки конечной точки API для получения списка собственных квизов
     */
    @RequestMapping("/my", method = [RequestMethod.GET])
    fun myQuizzes(request: HttpServletRequest): ResponseEntity<ShortQuizListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(quizAssembler.toShortQuizListVO(quizService.getMyQuizzes(user)))
    }

    /***
     * Метод обработки конечной точки API для получения квиза по его id
     */
    @RequestMapping("/{quiz_id}", method = [RequestMethod.GET])
    fun getQuiz(@PathVariable("quiz_id") quizId: Long): ResponseEntity<QuizVO> {
        val quiz = quizService.getQuiz(quizId) as Quiz
        val quizItems = quizService.getItemsByQuizId(quizId)
        val quizAnswers = quizItems.mapTo(ArrayList()) { quizService.getAnswersByQuizItemId(it.id) }
        return ResponseEntity.ok(quizAssembler.toQuizVO(quiz, quizItems, quizAnswers))
    }

    /***
     * Метод обработки конечной точки API для создания квиза
     */
    @PostMapping
    fun createQuiz(@RequestBody quizRO: QuizRO, request: HttpServletRequest): ResponseEntity<QuizVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        val quiz = quizService.createQuiz(user, quizRO)
        return getQuiz(quiz.id)
    }

    /***
     * Метод обработки конечной точки API для редактирования квиза по его id
     */
    @RequestMapping("/update", method = [RequestMethod.PUT])
    fun updateQuiz(
        @RequestBody quizRO: QuizRO,
        @RequestParam("id") id: Long,
        request: HttpServletRequest
    ): ResponseEntity<QuizVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        val quiz = quizService.updateQuiz(user, quizRO, id)
        return getQuiz(quiz.id)
    }

    /***
     * Метод обработки конечной точки API для удаления квиза по его id
     */
    @RequestMapping("/delete", method = [RequestMethod.DELETE])
    fun deleteQuiz(@RequestParam("id") id: Long, request: HttpServletRequest): ResponseEntity<String> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        quizService.deleteQuiz(user, id)
        return ResponseEntity.ok("Successfully deleted!")
    }

    /***
     * Метод обработки конечной точки API для записи резултата прохождения квиза
     */
    @RequestMapping("/result", method = [RequestMethod.POST])
    fun saveResult(
        @RequestParam("achieved") achieved: Int,
        @RequestParam("id") id: Long,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        quizService.saveQuizResult(user, achieved, id)
        return ResponseEntity.ok("Result successfully saved!")
    }

    /***
     * Метод обработки конечной точки API для получения списка резултатов прохождения квизов
     */
    @RequestMapping("/results", method = [RequestMethod.GET])
    fun getResults(request: HttpServletRequest): ResponseEntity<QuizResultListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(quizAssembler.toQuizResultListVO(quizService.getQuizResults(user)))
    }

    /***
     * Метод обработки конечной точки API для получения рейтинга пользователей прошедших квиз с ID
     */
    @RequestMapping("/top", method = [RequestMethod.GET])
    fun getTop(@RequestParam("id") quizId: Long, request: HttpServletRequest): ResponseEntity<TopVO> {
        return ResponseEntity.ok(quizAssembler.toTopVO(quizService.getTop(quizId)))
    }
}