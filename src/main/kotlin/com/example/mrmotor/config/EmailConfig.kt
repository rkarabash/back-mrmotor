package com.example.mrmotor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class EmailConfig(){
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.titan.email"
        mailSender.port = 465
        mailSender.username = "support@mrmotor.works"
        mailSender.password = "mrmotor228"
        val props: Properties = mailSender.javaMailProperties
        props.put("mail.transport.protocol", "smtp")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.debug", "true")
        props.put("mail.smtp.ssl.enable", "true")
        props.put("mail.smtp.ssl.trust", "*")
        return mailSender
    }
}
