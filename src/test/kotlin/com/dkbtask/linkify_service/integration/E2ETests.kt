package com.dkbtask.linkify_service.integration

import com.jayway.jsonpath.JsonPath
import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class E2ETests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @ParameterizedTest
    @ValueSource(
        strings = [
            "http://awesome-url.io",
            "https://www.another-awesome.com/path?query=param#fragment",
            "https://localhost:8000/",
            "http://192.168.0.1/test/a/b/c/d/e",
            "ftp://localhost:8000",
            "file://somepath/subpath/",
        ]
    )
    fun `should shorten a valid URL`(url: String) {
        val requestBody = """
            {
                "url": "$url"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/v1/url-service/shorten")
                .contentType("application/json")
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortUrlIdentifier").isNotEmpty)
    }

    @Test
    fun `should return full URL for a valid short URL`() {
        val sampleUrl = "https://another-awesome-linkify-service-url.io"
        val requestBody = """
            {
                "url": "$sampleUrl"
            }
        """.trimIndent()
        // Send request to shorten a URL.
        val shortenResponse = mockMvc.perform(
            post("/api/v1/url-service/shorten")
                .contentType("application/json")
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andReturn()

        val shortUrlIdentifier = JsonPath.read<String>(shortenResponse.response.contentAsString, "$.shortUrlIdentifier")

        // Then resolve it
        mockMvc.perform(get("/api/v1/url-service/resolve/$shortUrlIdentifier"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.longUrl", equalTo(sampleUrl)))
    }

    @Test
    fun `should return 404 for when long Url is not found for shortCodeIdentifier`() {
        val randomShortUrlIdentifier = NanoIdUtils.randomNanoId(12)

        mockMvc.perform(get("/api/v1/url-service/resolve/$randomShortUrlIdentifier"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errorCode").isNotEmpty)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "invalid-url.com/", "//random/url/"])
    fun `should return 403 for invalid URL`(url: String) {
        val requestBody = """
            {
                "url": "$url"
            }
        """.trimIndent()
        // Send request to shorten a URL.
        mockMvc.perform(post("/api/v1/url-service/shorten")
                .contentType("application/json")
                .content(requestBody)
        )
        .andExpect(status().isBadRequest)
        .andExpect(jsonPath("$.errorCode", equalTo("INVALID_LINK_PROVIDED")))
    }
}