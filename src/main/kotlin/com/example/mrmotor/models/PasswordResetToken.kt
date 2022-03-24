package com.example.mrmotor.models

import java.util.*
import javax.persistence.*

@Entity
class PasswordResetToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    var token: String = "",
    @OneToOne(optional = false)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    val user: User,
    @Column(nullable = false)
    var expiryDate: Date = Calendar.getInstance().time
){
    fun isExpired(): Boolean{
        return Date().after(expiryDate)
    }
}