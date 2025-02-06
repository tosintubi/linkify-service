package com.dkbtask.linkify_service.service

import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.exception.InvalidLinkException
import com.dkbtask.linkify_service.exception.ShortUrlNotFoundException
import com.dkbtask.linkify_service.model.Url
import com.dkbtask.linkify_service.repository.UrlRepository
import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils
import java.net.URL
import java.time.Instant
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.slf4j.MDC
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UrlService(
    private val urlRepository: UrlRepository,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private const val SHORT_CODE_SIZE = 12
    }

    @CachePut(value = ["shortUrls"], key = "#result.shortUrl")
    fun saveUrl(urlRequest: UrlRequest): Url {
        if (!isValidUrl(urlRequest.url)) {
            throw InvalidLinkException("Invalid URL")
        }

        val hashedUrl = generateShortUrlIdentifier()
        // does hashedurlExist
        // if exists, regenerate, otherwise continue
        logger.info { "Saving URL: ${urlRequest.url}" }
        val savedUrlCode = urlRepository.save(
            Url(
                shortUrl = hashedUrl,
                longUrl = urlRequest.url,
                createdAt = Instant.now()
            )
        )
        MDC.put("shortUrlIdentifier", savedUrlCode.shortUrl)
        logger.info { "💾 Successfully saved URL: ${savedUrlCode.shortUrl}." }

        return savedUrlCode
    }

    @Cacheable(value = ["shortUrls"], key = "#shortUrl")
    fun fetchLongUrl(shortUrl: String): Url {
        logger.info { "processing resolving short URL. $shortUrl" }

        if (StringUtils.isBlank(shortUrl)) {
            throw InvalidLinkException("Short Url code is blank or null")
        }

        val url = urlRepository.findById(shortUrl)
            .orElseThrow { ShortUrlNotFoundException("Couldn't find shortUrl: $shortUrl") }

        MDC.put("shortUrl", url.shortUrl)
        logger.info { "Successfully Fetched : $url" }
        return url
    }

    fun generateShortUrlIdentifier(): String {
        val dictionary = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return NanoIdUtils.randomNanoId(dictionary, SHORT_CODE_SIZE)
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val parsedUrl = URL(url)
            parsedUrl.protocol in listOf("http", "https", "ftp", "file")
            true
        } catch (e: Exception) {
            false
        }
    }
}
