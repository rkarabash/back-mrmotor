package com.example.mrmotor.service

import com.example.mrmotor.repositories.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.ArrayList
@Component
class AppUserDetailsService(val userRepository: UserRepository) :
    UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?:
        throw UsernameNotFoundException("A user with the username $username doesn't exist")
            return User(user.email, user.password,
                ArrayList())
    }
}
