package com.example.mrmotor.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.Date
import java.util.Collections.emptyList

/***
 * Объект, реализующий бизнес логику работы с аутентификацией доступа к REST API c помощью JWT токена
 */
internal object TokenAuthenticationService {
    private val TOKEN_EXPIRY: Long = 864000000
    private val SECRET = "$78gr43g7g8feb8we"
    private val TOKEN_PREFIX = "Bearer"
    private val AUTHORIZATION_HEADER_KEY = "Authorization"

    /***
     * Метод, добавляющий аутентификационный заголовок в ответ на запрос
     */
    fun addAuthentication(res: HttpServletResponse, username: String) {

        res.addHeader(AUTHORIZATION_HEADER_KEY, "$TOKEN_PREFIX ${getJWT(username)}")
    }

    /***
     * Метод, для получения JWT токена по юзернейму
     */
    fun getJWT(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(
                Date(
                    System.currentTimeMillis() +
                            TOKEN_EXPIRY
                )
            )
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact()
    }

    /***
     * Метод, авторизующий запрос в API
     */
    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(AUTHORIZATION_HEADER_KEY)
        if (token != null) {
            val user = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .body.subject
            if (user != null)
                return UsernamePasswordAuthenticationToken(
                    user, null,
                    emptyList()
                )
        }
        return null
    }
}

