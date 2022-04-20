package com.example.mrmotor.service

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User

/***
 * Интерфейс, определяющий бизнес логику работы с информационными постами
 */
interface PostService {
    fun listPosts(): List<Post>
    fun getPostData(id: Long): Post?
    fun getPostsByType(type: PostType): List<Post>
    fun getPostsByTypeWithLimitation(type: PostType, offset: Int, limit: Int): List<Post>
    fun getNews(): List<Post>
    fun getNewsWithLimitation( offset: Int, limit: Int): List<Post>
    fun updatePost(postDetails: Post, id: Long): Post
    fun removePost(id: Long): Boolean
    fun createPost(postDetails: Post): Post
    fun like(currentUser: User, id: Long): Boolean
    fun getLikedPosts(currentUser: User): List<Post>
    fun search(query: String): List<Post>
}