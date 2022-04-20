package com.example.mrmotor.controller

import com.example.mrmotor.components.PostAssembler
import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.PostListVO
import com.example.mrmotor.objects.PostVO
import com.example.mrmotor.repositories.LikeRepository
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/***
 * Класс-контроллер, отвечающий за обработку запросов, связанных с работой бизнес логики информационнных постов
 */
@RestController
@CrossOrigin
@RequestMapping("/posts")
class PostController(
    val postService: PostService,
    val likeRepository: LikeRepository,
    val postAssembler: PostAssembler,
    val userRepository: UserRepository
) {
    /***
     * Метод для получения списка пометок "Нравится"
     */
    private fun getLikes(posts: List<Post>, request: HttpServletRequest): List<Boolean> {
        var likes = posts.map { false };
        if (request.getHeader("Authorization") != null) {
            val user = userRepository.findByEmail(request.userPrincipal.name) as User
            val tmp = likeRepository.findByUserId(user.id).map { it.post.id }
            likes = posts.map { it.id in tmp }
        }
        return likes
    }

    /***
     * Метод для получения пометки "Нравится"
     */
    private fun getLike(post: Post, request: HttpServletRequest): Boolean {
        if (request.getHeader("Authorization") != null) {
            val user = userRepository.findByEmail(request.userPrincipal.name) as User
            val like = likeRepository.findByPostIdAndUserId(post.id, user.id)
            return like != null
        }
        return false;
    }

    /***
     * Метод обработки конечной точки API для получения списка информационных постов
     */
    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<PostListVO> {
        val posts = postService.listPosts();
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }

    /***
     * Метод обработки конечной точки API для получения информационного поста по его id
     */
    @RequestMapping("/get", method = [RequestMethod.GET])
    fun getPost(@RequestParam("id") id: Long, request: HttpServletRequest): ResponseEntity<PostVO> {
        val post = postService.getPostData(id)
        return ResponseEntity.ok(postAssembler.toPostVO(post!!, getLike(post, request)))
    }

    /***
     * Метод обработки конечной точки API для получения списка информационных постов по заданному типу
     */
    @RequestMapping("/get_by_type", method = [RequestMethod.GET])
    fun getPostByType(@RequestParam("type") type: PostType, request: HttpServletRequest): ResponseEntity<PostListVO> {
        val posts = postService.getPostsByType(type)
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }

    /***
     * Метод обработки конечной точки API для получения списка информационных постов по заданному типу с ограничением на кол-во
     */
    @RequestMapping("/get_by_type_limit", method = [RequestMethod.GET])
    fun getPostByTypeAndLimitation(
        @RequestParam("type") type: PostType,
        @RequestParam("offset") offset: Int,
        @RequestParam("limit") limit: Int,
        request: HttpServletRequest
    ): ResponseEntity<PostListVO> {
        val posts = postService.getPostsByTypeWithLimitation(type, offset, limit)
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }

    /***
     * Метод обработки конечной точки API для получения списка новостей
     */
    @RequestMapping("/news", method = [RequestMethod.GET])
    fun getNews(request: HttpServletRequest): ResponseEntity<PostListVO> {
        val posts = postService.getNews()
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }

    /***
     * Метод обработки конечной точки API для получения списка новостей с ограничением на кол-во
     */
    @RequestMapping("/news_limit", method = [RequestMethod.GET])
    fun getNewsAndLimitation(
        @RequestParam("offset") offset: Int,
        @RequestParam("limit") limit: Int,
        request: HttpServletRequest
    ): ResponseEntity<PostListVO> {
        val posts = postService.getNewsWithLimitation(offset, limit)
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }

    /***
     * Метод обработки конечной точки API для редактирования информационного поста по его id
     */
    @RequestMapping("/update", method = [RequestMethod.PUT])
    fun updatePost(@RequestParam("id") id: Long, @RequestBody post: Post): ResponseEntity<Post> {
        return ResponseEntity.ok(postService.updatePost(post, id))
    }

    /***
     * Метод обработки конечной точки API для удаления информационного поста по его id
     */
    @RequestMapping("/delete", method = [RequestMethod.DELETE])
    fun deletePost(@RequestParam("id") id: Long): ResponseEntity<String> {
        postService.removePost(id)
        return ResponseEntity.ok("Successfully deleted!")
    }

    /***
     * Метод обработки конечной точки API для создания информационного поста
     */
    @PostMapping
    fun createPost(@RequestBody post: Post): ResponseEntity<Post> {
        return ResponseEntity.ok(postService.createPost(post))
    }

    /***
     * Метод обработки конечной точки API для установки пометки "Нравится" информационному посту по его id
     */
    @RequestMapping("/like", method = [RequestMethod.POST])
    fun like(@RequestParam("id") id: Long, request: HttpServletRequest): ResponseEntity<String> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(if (postService.like(user, id)) "Successfully Liked!" else "Successfully Unliked!")
    }

    /***
     * Метод обработки конечной точки API для получения списка понравившихся постов
     */
    @RequestMapping("/liked", method = [RequestMethod.GET])
    fun getLiked(request: HttpServletRequest): ResponseEntity<PostListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        val posts = postService.getLikedPosts(user)
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, posts.map { true }))
    }

    /***
     * Метод обработки конечной точки API для получения списка информационных постов по поискому запросу
     */
    @RequestMapping("search", method = [RequestMethod.GET])
    fun search(@RequestParam("query") query: String, request: HttpServletRequest): ResponseEntity<PostListVO> {
        val posts = postService.search(query)
        return ResponseEntity.ok(postAssembler.toPostListVO(posts, getLikes(posts, request)))
    }
}