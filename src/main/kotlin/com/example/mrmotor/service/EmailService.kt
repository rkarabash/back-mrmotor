package com.example.mrmotor.service

import com.example.mrmotor.objects.MailVO
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.nio.charset.StandardCharsets

/***
 * Класс, реализующий бизнес логику для отправления Email-сообщения со сбросом пароля
 */
@Service
class EmailService(
    val emailSender: JavaMailSender,
    val templateEngine: SpringTemplateEngine
) {
    /***
     * Метод для отправки Email-сообщения со сбросом пароля
     */
    fun sendMail(mail: MailVO) {
        val message = emailSender.createMimeMessage()
        val helper =
            MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())
        val context = Context()
        context.setVariables(mail.model)
        val html = templateEngine.process("email/email-template2", context)
        helper.setTo(mail.to)
        helper.setText(html, true)
        helper.setSubject(mail.subject)
        helper.setFrom(mail.from)
        emailSender.send(message)
    }
}