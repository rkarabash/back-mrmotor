package com.example.mrmotor.filters

import com.example.mrmotor.security.AccountCredentials
import com.example.mrmotor.service.TokenAuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.bind.annotation.CrossOrigin
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

/***
 * Класс-фильтр, ответственный за аутентификацию в REST API
 */
@CrossOrigin
class JWTLoginFilter(url: String, authManager: AuthenticationManager) :
    AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {
    init {
        authenticationManager = authManager
    }

    /***
     * Метод, производящий авторизацию пользователя
     */
    @Throws(
        AuthenticationException::class, IOException::class,
        ServletException::class
    )
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication {
        val credentials = ObjectMapper()
            .readValue(req.inputStream, AccountCredentials::class.java)

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentials.username,
                credentials.password,
                emptyList()
            )
        )
    }

    /***
     * Метод, передающий запрос дальше при успешной авторизации
     */
    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse, chain: FilterChain,
        auth: Authentication
    ) {
        res.setHeader("Access-Control-Allow-Origin", "*")
        TokenAuthenticationService.addAuthentication(res, auth.name)
    }
}
