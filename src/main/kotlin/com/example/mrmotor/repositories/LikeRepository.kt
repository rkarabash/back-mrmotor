package com.example.mrmotor.repositories

import com.example.mrmotor.models.Like
import org.springframework.data.repository.CrudRepository

interface LikeRepository: CrudRepository<Like, Long> {
    fun findByPostId(id: Long): List<Like>
    fun findByUserId(id: Long): List<Like>
    fun findByPostIdAndUserId(postId: Long, userId: Long): Like?
}