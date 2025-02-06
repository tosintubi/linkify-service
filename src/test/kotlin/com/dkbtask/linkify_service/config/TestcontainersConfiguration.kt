package com.dkbtask.linkify_service.config

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest
class TestcontainersConfiguration {

	companion object {
		@Container
		val postgresContainer: PostgreSQLContainer<out PostgreSQLContainer<*>> = PostgreSQLContainer("postgres:15.3").apply {
			withDatabaseName("linkifyservice")
			withUsername("postgres")
			withPassword("password")
		}

		@DynamicPropertySource
		fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
			registry.add("spring.datasource.username", postgresContainer::getUsername)
			registry.add("spring.datasource.password", postgresContainer::getPassword)
		}
		@Container
		val redisContainer = GenericContainer<Nothing>("redis:latest").apply {
			withExposedPorts(6379)
		}

		@DynamicPropertySource
		fun redisProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.redis.host") { redisContainer.host }
			registry.add("spring.redis.port") { redisContainer.getMappedPort(6399) }
		}
	}
}
