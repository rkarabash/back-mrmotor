package com.example.mrmotor.config

import com.example.mrmotor.filters.JWTAuthenticationFilter
import com.example.mrmotor.filters.JWTLoginFilter
import com.example.mrmotor.service.AppUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsUtils


/***
 * Класс-конфигурация Spring Security
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(val userDetailsService: AppUserDetailsService) : WebSecurityConfigurerAdapter() {
    /***
     * Метод, конфигурирующий AuthenticationManager
     */
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    /***
     * Метод, конфигурирующий параметры безопасности
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .httpBasic().disable()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/ulogin").permitAll()
            .antMatchers(HttpMethod.POST, "/users/forgot_password")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/users/reset_password")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/users/registrations")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/news")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/news_limit**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/get**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/get_by_type**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/get_by_type_limit**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/posts/search**")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/quiz")
            .permitAll()
            .antMatchers(HttpMethod.GET, "/quiz/limit**")
            .permitAll()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(
                JWTLoginFilter(
                    "/login",
                    authenticationManager()
                ),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterBefore(
                JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }

    /***
     * Метод, конфигурирующий хэширование пароля
     */
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(BCryptPasswordEncoder())
    }
}
