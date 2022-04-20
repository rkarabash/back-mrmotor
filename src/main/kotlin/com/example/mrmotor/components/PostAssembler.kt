package com.example.mrmotor.components

import com.example.mrmotor.models.Post
import com.example.mrmotor.objects.PostListVO
import com.example.mrmotor.objects.PostVO
import org.springframework.stereotype.Component

/***
 * Класс, отвечающий за приведение объектов информационных постов в объекты-значения
 */
@Component
class PostAssembler {
    /***
     * Метод, конвертирующий объект информационного поста и пометки "Нравится" в объект-значение PostVO
     */
    fun toPostVO(post: Post, like: Boolean): PostVO {
        return PostVO(
            post.id, post.title, post.source, post.content, post.type, post.thumbnail, like
        )
    }

    /***
     * Метод, конвертирующий список объектов информационного поста и объектов пометки "Нравится" в объект-значение PostListVO
     */
    fun toPostListVO(posts: List<Post>, likes: List<Boolean>): PostListVO {
        return PostListVO(
            posts.mapIndexed { index, post -> toPostVO(post, likes[index]) }
        )
    }
}