package com.example.mrmotor.components


import com.example.mrmotor.objects.UserListVO
import com.example.mrmotor.objects.UserVO
import com.example.mrmotor.models.User
import org.springframework.stereotype.Component

@Component
class UserAssembler {
    fun toUserVO(user: User): UserVO {
        return UserVO(
            user.id, user.email,
            user.name, user.avatar
        )
    }

    fun toUserListVO(users: List<User>): UserListVO {
        val userVOList = users.map { toUserVO(it) }
        return UserListVO(userVOList)
    }
}
