package com.example.mrmotor.filters

import com.example.mrmotor.service.TokenAuthenticationService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import java.io.IOException

/***
 * Класс-фильтр, ответственный за фильтрацию разрешенных и несанкционированных запросов к REST API
 */
class JWTAuthenticationFilter : GenericFilterBean() {
    /***
     * Метод, производящий фильтрацию запросов
     */
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = TokenAuthenticationService
            .getAuthentication(request as HttpServletRequest)
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}