package com.dkbtask.linkify_service

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
	servers = [Server(url = "/linkifyservice/")],
	info = Info(
		description = "API documentation for Linkify - our awesome URL Shortener service",
		version = "1.0.0",
		license = License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
class LinkifyServiceApplication

fun main(args: Array<String>) {
	runApplication<LinkifyServiceApplication>(*args)
}
