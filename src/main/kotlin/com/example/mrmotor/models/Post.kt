package com.example.mrmotor.models

import com.example.mrmotor.constants.PostType
import javax.persistence.*

/***
 * Класс-сущность, представляющий объект информационного поста
 */
@Entity
@Table(name = "`post`")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var title: String = "Title",
    var source: String = "",
    @Column(columnDefinition = "TEXT")
    var content: String = "",
    @Enumerated(EnumType.STRING)
    var type: PostType = PostType.NEWS,
    @Column(columnDefinition = "TEXT")
    var thumbnail: String = "",
){
    @OneToMany(mappedBy = "post", targetEntity = Like::class)
    private var likes: Collection<Like>? = null
}

