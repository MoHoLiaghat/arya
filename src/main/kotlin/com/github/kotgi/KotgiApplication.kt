package com.github.kotgi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan("com.github.kotgi")
@SpringBootApplication
class KotgiApplication

fun main(args: Array<String>) {
    runApplication<KotgiApplication>(*args)
}
