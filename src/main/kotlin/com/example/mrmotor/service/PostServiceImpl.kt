package com.example.mrmotor.service

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.exceptions.InvalidPostIdException
import com.example.mrmotor.exceptions.NoLikeFoundException
import com.example.mrmotor.exceptions.PostDataEmptyException
import com.example.mrmotor.exceptions.SealedLikeFoundException
import com.example.mrmotor.models.Like
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User
import com.example.mrmotor.repositories.LikeRepository
import com.example.mrmotor.repositories.PostRepository
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class PostServiceImpl(val repository: PostRepository, val likeRepository: LikeRepository) : PostService {
    override fun listPosts(): List<Post> {
        return repository.findAll().mapTo(ArrayList()) { it }
    }

    @Throws(InvalidPostIdException::class)
    override fun getPostData(id: Long): Post? {
        val postOptional = repository.findById(id)
        if (postOptional.isPresent)
            return postOptional.get()
        throw InvalidPostIdException("Post with id of '$id' does not exist.")
    }

    override fun getPostsByType(type: PostType): List<Post> {
        return repository.findByType(type);
    }

    override fun getNews(): List<Post> {
        return getPostsByType(PostType.NEWS)
    }

    @Throws(
        PostDataEmptyException::class, InvalidPostIdException::class
    )
    override fun updatePost(postDetails: Post, id: Long): Post {
        if (!repository.findById(id).isPresent)
            throw InvalidPostIdException("Post with id of '$id' does not exist.")

        if (postDetails.title.isNotEmpty() && postDetails.content.isNotEmpty()) {
            val post = repository.findById(id).get()
            post.title = postDetails.title
            post.content = postDetails.content
            post.source = postDetails.source
            post.type = postDetails.type
            post.thumbnail = postDetails.thumbnail
            repository.save(post)
            return post
        }
        throw PostDataEmptyException("Title and Content must be not empty.")
    }

    @Throws(InvalidPostIdException::class, SealedLikeFoundException::class)
    override fun removePost(id: Long): Boolean {
        val postOptional = repository.findById(id)
        if (postOptional.isPresent) {
            val likes = likeRepository.findByPostId(id)
            likeRepository.deleteAll(likes)
            if (likeRepository.findByPostId(id).isNotEmpty())
                throw SealedLikeFoundException("Deleting likes with post is required")
            repository.deleteById(id)
            return true
        }
        throw InvalidPostIdException("Post with id of '$id' does not exist.")
    }

    @Throws(PostDataEmptyException::class)
    override fun createPost(postDetails: Post): Post {
        if (postDetails.title.isNotEmpty() && postDetails.content.isNotEmpty()) {
            val post = Post()
            post.title = postDetails.title
            post.content = postDetails.content
            post.source = postDetails.source
            post.type = postDetails.type
            post.thumbnail = postDetails.thumbnail
            repository.save(post)
            return post
        }
        throw PostDataEmptyException("Title and Content must be not empty.")
    }

    @Throws(InvalidPostIdException::class)
    override fun like(currentUser: User, id: Long): Boolean {
        val postOptional = repository.findById(id)
        if (postOptional.isPresent) {
            val like = Like(currentUser, postOptional.get())
            likeRepository.save(like)
            return true
        }
        throw InvalidPostIdException("Post with id of '$id' does not exist.")
    }

    @Throws(NoLikeFoundException::class)
    override fun unlike(currentUser: User, id: Long): Boolean {
        var like = likeRepository.findByPostIdAndUserId(id, currentUser.id)
        if (like != null) {
            likeRepository.deleteById(like.id)
            return true
        }
        throw NoLikeFoundException("The user '${currentUser.name}' has not like to post with id '$id'")
    }

    override fun getLikedPosts(currentUser: User): List<Post> {
        var likes = likeRepository.findByUserId(currentUser.id)
        return likes.mapTo(ArrayList()) { it.post }
    }

}