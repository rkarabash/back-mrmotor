package com.example.mrmotor.repositories

import com.example.mrmotor.constants.PostType
import com.example.mrmotor.models.Post
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/***
 * Интерфейс, определенный для доступа к таблице с сущностями информационного поста
 */
interface PostRepository : CrudRepository<Post, Long> {
    /***
     * Метод для получение из БД списка информационных постов по типу поста
     */
    fun findByTypeOrderByIdDesc(type: PostType): List<Post>

    /***
     * Метод для получение из БД списка информационных постов по поискому запросу
     */
    fun findByTitleContainingIgnoreCaseOrderByType(search: String): List<Post>

    /***
     * Метод для получение из БД списка информационных постов по типу поста с ограничением на кол-во
     */
    @Query(
        value = "select * from post p where p.type = :type order by p.id desc offset :offset limit :limit",
        nativeQuery = true
    )
    fun findByTypeAndMore(
        @Param("type") type: String,
        @Param("offset") offset: Int,
        @Param("limit") limit: Int
    ): List<Post>
}