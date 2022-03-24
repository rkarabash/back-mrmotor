package com.example.mrmotor.service

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User

interface PostService {
    fun listPosts(): List<Post>
    fun getPostData(id: Long): Post?
    fun getPostsByType(type: PostType): List<Post>
    fun getNews(): List<Post>
    fun updatePost(postDetails: Post, id: Long): Post
    fun removePost(id: Long): Boolean
    fun createPost(postDetails: Post): Post
    fun like(currentUser: User, id: Long): Boolean
    fun unlike(currentUser: User, id: Long): Boolean
    fun getLikedPosts(currentUser: User): List<Post>
}