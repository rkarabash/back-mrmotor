package com.example.mrmotor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


/***
 * Класс-конфигурация Spring Mail
 */
@Configuration
class EmailConfig() {
    /***
     * Метод, конфигурирующий JavaMailSender
     */
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = "email"
        mailSender.password = "password"
        val props: Properties = mailSender.javaMailProperties
//        props.put("mail.transport.protocol", "smtp")
//        props.put("mail.smtp.auth", "true")
//        props.put("mail.smtp.starttls.enable", "true")
//        props.put("mail.debug", "true")
//        props.put("mail.smtp.ssl.enable", "true")
//        props.put("mail.smtp.ssl.trust", "*")
        props.put("mail.transport.protocol", "smtp")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.debug", "true")
        return mailSender
    }
}
