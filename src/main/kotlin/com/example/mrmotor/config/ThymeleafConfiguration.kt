package com.example.mrmotor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver


/***
 * Класс-конфигурация Spring ThymeLeaf
 */
@Configuration
@EnableWebMvc
class ThymeleafConfiguration {
    /***
     * Метод, конфигурирующий SpringTemplateEngine
     */
    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(thymeLeafTemplateResolver())
        return templateEngine
    }

    /***
     * Метод, конфигурирующий SpringResourceTemplateResolver
     */
    @Bean
    fun thymeLeafTemplateResolver(): SpringResourceTemplateResolver {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.prefix = "classpath:templates/"
        templateResolver.suffix = ".html"
        templateResolver.setTemplateMode("HTML")
        return templateResolver
    }
}