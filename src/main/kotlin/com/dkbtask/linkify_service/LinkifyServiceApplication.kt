package com.dkbtask.linkify_service

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@OpenAPIDefinition(
	info = Info(
		description = "API documentation for Linkify - our awesome URL Shortener service",
		version = "1.0.0")
)
@EnableCaching
class LinkifyServiceApplication

fun main(args: Array<String>) {
	runApplication<LinkifyServiceApplication>(*args)
}
