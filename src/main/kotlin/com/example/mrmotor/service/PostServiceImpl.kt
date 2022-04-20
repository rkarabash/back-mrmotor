package com.example.mrmotor.service

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.exceptions.InvalidPostIdException
import com.example.mrmotor.exceptions.PostDataEmptyException
import com.example.mrmotor.exceptions.SealedLikeFoundException
import com.example.mrmotor.models.Like
import com.example.mrmotor.models.Post
import com.example.mrmotor.models.User
import com.example.mrmotor.repositories.LikeRepository
import com.example.mrmotor.repositories.PostRepository
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/***
 * Класс, реализующий бизнес логику работы с информационными постами
 */
@Service
class PostServiceImpl(val repository: PostRepository, val likeRepository: LikeRepository) : PostService {
    /***
     * Метод бизнес-логики для получения списка информационных постов
     */
    override fun listPosts(): List<Post> {
        return repository.findAll().mapTo(ArrayList()) { it }
    }

    /***
     * Метод бизнес-логики для получения информационного поста по id
     */
    @Throws(InvalidPostIdException::class)
    override fun getPostData(id: Long): Post? {
        val postOptional = repository.findById(id)
        if (postOptional.isPresent)
            return postOptional.get()
        throw InvalidPostIdException("Post with id of '$id' does not exist.")
    }

    /***
     * Метод бизнес-логики для получения списка информационных постов по типу поста
     */
    override fun getPostsByType(type: PostType): List<Post> {
        return repository.findByTypeOrderByIdDesc(type);
    }

    /***
     * Метод бизнес-логики для получения списка информационных постов по типу поста с ограничением на кол-во
     */
    override fun getPostsByTypeWithLimitation(type: PostType, offset: Int, limit: Int): List<Post> {
        return repository.findByTypeAndMore(type.name, offset, limit);
    }

    /***
     * Метод бизнес-логики для получения списка новостей
     */
    override fun getNews(): List<Post> {
        return getPostsByType(PostType.NEWS)
    }

    /***
     * Метод бизнес-логики для получения списка новостей с ограничением на кол-во
     */
    override fun getNewsWithLimitation(offset: Int, limit: Int): List<Post> {
        return getPostsByTypeWithLimitation(PostType.NEWS, offset, limit)
    }

    /***
     * Метод бизнес-логики для редактирования информационного поста по его id
     */
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

    /***
     * Метод бизнес-логики для удаления информационного поста по его id
     */
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

    /***
     * Метод бизнес-логики для создания информационного поста
     */
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

    /***
     * Метод бизнес-логики для установки пометки "Нравится" информационному посту по его id
     */
    @Throws(InvalidPostIdException::class)
    override fun like(currentUser: User, id: Long): Boolean {
        val postOptional = repository.findById(id)
        if (postOptional.isPresent) {
            var like = likeRepository.findByPostIdAndUserId(id, currentUser.id)
            if (like != null) {
                likeRepository.delete(like)
                return false
            } else {
                like = Like(currentUser, postOptional.get())
                likeRepository.save(like)
                return true
            }
        }
        throw InvalidPostIdException("Post with id of '$id' does not exist.")
    }

    /***
     * Метод бизнес-логики для получения списка понравившихся постов
     */
    override fun getLikedPosts(currentUser: User): List<Post> {
        var likes = likeRepository.findByUserId(currentUser.id)
        return likes.mapTo(ArrayList()) { it.post }
    }

    /***
     * Метод бизнес-логики для получения списка информационных постов по поискому запросу
     */
    override fun search(query: String): List<Post> {
        return repository.findByTitleContainingIgnoreCaseOrderByType(query)
    }
}