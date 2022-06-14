package com.free4developer.sampleserver

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SampleServerApplication {

    @Bean
    fun beanObjectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(JavaTimeModule()).registerModule(KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

}

fun main(args: Array<String>) {
    runApplication<SampleServerApplication>(*args)
}