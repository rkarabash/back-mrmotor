package com.example.mrmotor.repositories

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository: CrudRepository<Post, Long> {
    fun findByType(type: PostType): List<Post>
}