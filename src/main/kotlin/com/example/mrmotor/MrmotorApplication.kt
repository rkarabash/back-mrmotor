package com.example.mrmotor

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

/***
 * Класс-инициализатор приложения
 */
@SpringBootApplication
class MrmotorApplication

fun main(args: Array<String>) {
    runApplication<MrmotorApplication>(*args)
}
