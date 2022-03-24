package com.example.mrmotor.models

import javax.persistence.*

@Entity
@Table(name = "`like`")
class Like(
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User,
    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    var post: Post,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
)