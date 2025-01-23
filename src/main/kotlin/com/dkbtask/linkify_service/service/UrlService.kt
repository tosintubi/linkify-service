package com.dkbtask.linkify_service.service

import com.dkbtask.linkify_service.dto.LongUrlResponse
import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.exception.InvalidLinkException
import com.dkbtask.linkify_service.exception.ShortUrlNotFoundException
import com.dkbtask.linkify_service.model.Url
import com.dkbtask.linkify_service.model.toLongUrlResponse
import com.dkbtask.linkify_service.repository.UrlRepository
import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils
import java.net.URL
import java.time.Instant
import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.slf4j.MDC
import org.springframework.stereotype.Service

@Service
class UrlService (
    private val urlRepository: UrlRepository
){
    companion object {
        private val logger = KotlinLogging.logger {}
        private const val SHORT_CODE_SIZE = 12
    }

    fun saveUrl(urlRequest: UrlRequest): String {
        if (!isValidUrl(urlRequest.url)){
            throw InvalidLinkException("Invalid URL")
        }

        logger.info { "Saving URL: ${urlRequest.url}" }
        val savedUrlCode = urlRepository.save(
            Url(
                shortUrl = generateShortUrlCode(),
                longUrl = urlRequest.url,
                createdAt = Instant.now())
        )
            .also{
                MDC.put("shortUrl", it.shortUrl )
                logger.info { "Successfully saved URL: ${it.shortUrl}"
                }
            }

        return savedUrlCode.shortUrl
    }

    fun fetchLongUrl(shortUrl: String): LongUrlResponse {
        logger.info {"processing resolving short URL. $shortUrl" }

        if (StringUtils.isBlank(shortUrl)) {
            throw InvalidLinkException("Short Url code is blank or null")
        }

        val url = urlRepository.findById(shortUrl)
            .orElseThrow { ShortUrlNotFoundException("Couldn't find shortUrl: $shortUrl") }

        MDC.put("shortUrl", url.shortUrl )
        logger.info { "Successfully Fetched : $url" }
        return url.toLongUrlResponse()
    }

    fun generateShortUrlCode(): String {
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
