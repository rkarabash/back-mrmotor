package com.example.mrmotor.components


import com.example.mrmotor.objects.UserListVO
import com.example.mrmotor.objects.UserVO
import com.example.mrmotor.models.User
import org.springframework.stereotype.Component


/***
 * Класс, отвечающий за приведение объектов пользователей в объекты-значения
 */
@Component
class UserAssembler {
    /***
     * Метод, конвертирующий в объект-значение UserVO
     */
    fun toUserVO(user: User): UserVO {
        return UserVO(
            user.id, user.email,
            user.name, user.avatar
        )
    }

    /***
     * Метод, конвертирующий в объект-значение UserListVO
     */
    fun toUserListVO(users: List<User>): UserListVO {
        val userVOList = users.map { toUserVO(it) }
        return UserListVO(userVOList)
    }
}
