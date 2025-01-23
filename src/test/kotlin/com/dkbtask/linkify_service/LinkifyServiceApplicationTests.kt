package com.dkbtask.linkify_service

import com.dkbtask.linkify_service.config.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class LinkifyServiceApplicationTests {

	@Test
	fun contextLoads() {
	}

}
