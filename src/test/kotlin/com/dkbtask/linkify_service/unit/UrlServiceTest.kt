package com.dkbtask.linkify_service.unit


import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.exception.InvalidLinkException
import com.dkbtask.linkify_service.exception.ShortUrlNotFoundException
import com.dkbtask.linkify_service.model.Url
import com.dkbtask.linkify_service.repository.UrlRepository
import com.dkbtask.linkify_service.service.UrlService
import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness


@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UrlServiceTest {

    @Mock
    private lateinit var urlRepository: UrlRepository

    @InjectMocks
    private lateinit var urlService : UrlService

    @ParameterizedTest
    @ValueSource(strings = [
        "http://awesome-url.io",
        "https://www.another-awesome.com/path?query=param#fragment",
        "https://localhost:8000/",
        "http://192.168.0.1/test/a/b/c/d/e",
        "ftp://localhost:8000",
        "file://somepath/subpath/",
    ])
    fun `should save URL successfully`(testUrls: String) {
        val urlRequest = UrlRequest(testUrls)
        val shortUrl = NanoIdUtils.randomNanoId()
        val expectedUrl = Url(shortUrl, urlRequest.url, Instant.now())

        whenever(urlRepository.save(any())).thenReturn(expectedUrl)

        val savedShortLink = urlService.saveUrl(urlRequest)

        assertAll(
            { assertEquals(shortUrl, savedShortLink.shortUrl) },
            { assertEquals(urlRequest.url, savedShortLink.longUrl) },
            { assertNotNull(savedShortLink) }
        )
    }

    @Test
    fun `should throw BadRequestException for invalid URL`() {
        val urlRequest = UrlRequest("invalid_url")
        assertThrows<InvalidLinkException> {
            urlService.saveUrl(urlRequest)
        }
    }

    @Test
    fun `should fetch long URL successfully`() {
        val shortCode = "RAnDOM12oLI"
        val testUrl = Url(shortCode, "https://awesome-linkify.io/some-path/another/one", Instant.now())

        whenever(urlRepository.findById(shortCode)).thenReturn(Optional.of(testUrl))

        urlRepository.save(testUrl)
        val response = urlService.fetchLongUrl(shortCode)

        assertEquals(testUrl.longUrl, response.longUrl)
    }

    @Test
    fun `should throw ShortUrlNotFoundException for non-existent short URL`() {
        val shortUrl = "aW2_87cOlqZu61jt"

        assertThrows<ShortUrlNotFoundException> {
            urlService.fetchLongUrl(shortUrl)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "http:/invalid-url.com/",""])
    fun `should throw BadRequestException for empty short URL`() {
        assertThrows<InvalidLinkException> {
            urlService.fetchLongUrl("")
        }
    }
}