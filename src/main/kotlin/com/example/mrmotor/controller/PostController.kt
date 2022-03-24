package com.example.mrmotor.controller

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User
import com.example.mrmotor.objects.PostListVO
import com.example.mrmotor.repositories.UserRepository
import com.example.mrmotor.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
@RequestMapping("/posts")
class PostController(
    val postService: PostService,
    val userRepository: UserRepository
) {

    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<PostListVO> {
        return ResponseEntity.ok(PostListVO(postService.listPosts()))
    }

    @RequestMapping("/get", method = [RequestMethod.GET])
    fun getPost(@RequestParam("id") id: Long): ResponseEntity<Post> {
        return ResponseEntity.ok(postService.getPostData(id))
    }

    @RequestMapping("/get_by_type", method = [RequestMethod.GET])
    fun getPostByType(@RequestParam("type") type: PostType): ResponseEntity<PostListVO> {
        return ResponseEntity.ok(PostListVO(postService.getPostsByType(type)))
    }

    @RequestMapping("/news", method = [RequestMethod.GET])
    fun getNews(): ResponseEntity<PostListVO> {
        return ResponseEntity.ok(PostListVO(postService.getNews()))
    }

    @RequestMapping("/update", method = [RequestMethod.PUT])
    fun updatePost(@RequestParam("id") id: Long, @RequestBody post: Post): ResponseEntity<Post> {
        return ResponseEntity.ok(postService.updatePost(post, id))
    }

    @RequestMapping("/delete", method = [RequestMethod.DELETE])
    fun deletePost(@RequestParam("id") id: Long): ResponseEntity<String> {
        postService.removePost(id)
        return ResponseEntity.ok("Successfully deleted!")
    }

    @PostMapping
    fun createPost(@RequestBody post: Post): ResponseEntity<Post> {
        return ResponseEntity.ok(postService.createPost(post))
    }

    @RequestMapping("/like", method = [RequestMethod.POST])
    fun like(@RequestParam("id") id: Long, request: HttpServletRequest): ResponseEntity<String> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        postService.like(user, id)
        return ResponseEntity.ok("Successfully Liked!")
    }

    @RequestMapping("/unlike", method = [RequestMethod.DELETE])
    fun unlike(@RequestParam("id") id: Long, request: HttpServletRequest): ResponseEntity<String> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        postService.unlike(user, id)

        return ResponseEntity.ok("Successfully unliked!")
    }

    @RequestMapping("/liked", method = [RequestMethod.GET])
    fun getLiked(request: HttpServletRequest): ResponseEntity<PostListVO> {
        val user = userRepository.findByEmail(request.userPrincipal.name) as User
        return ResponseEntity.ok(PostListVO(postService.getLikedPosts(user)))
    }

}