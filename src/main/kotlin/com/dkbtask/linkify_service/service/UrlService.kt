package com.dkbtask.linkify_service.service

import com.dkbtask.linkify_service.dto.LongUrlResponse
import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.dto.ShortUrlResponse
import com.dkbtask.linkify_service.exception.BadRequestException
import com.dkbtask.linkify_service.exception.ShortUrlNotFoundException
import com.dkbtask.linkify_service.model.Url
import com.dkbtask.linkify_service.model.toLongUrlResponse
import com.dkbtask.linkify_service.model.toShortUrlResponse
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
    }

    fun saveUrl(urlRequest: UrlRequest): ShortUrlResponse {
        if (!isValidUrl(urlRequest.url)){
            throw BadRequestException("Invalid URL")
        }

        logger.info { "Saving URL: ${urlRequest.url}" }
        val savedUrl = urlRepository.save(
            Url(
                longUrl = generateShortUrlCode(),
                shortUrl = urlRequest.url,
                createdAt = Instant.now())
        )
            .also{
                MDC.put("shortUrl", it.shortUrl )
                logger.info { "Successfully saved URL: ${it.shortUrl}"
                }
            }

        return savedUrl.toShortUrlResponse()
    }

    fun fetchLongUrl(shortUrl: String): LongUrlResponse {
        logger.info {"processing resolving short URL. $shortUrl" }

        if (StringUtils.isBlank(shortUrl)) {
            throw BadRequestException("Short Url code is blank or null")
        }

        val url = urlRepository.findById(shortUrl)
            .orElseThrow { ShortUrlNotFoundException("Couldn't find shortUrl: $shortUrl") }

        MDC.put("shortUrl", url.shortUrl )
        logger.info { "Successfully Fetched : $url" }
        return url.toLongUrlResponse()
    }


    private fun generateShortUrlCode(): String {
        val dictionary = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return NanoIdUtils.randomNanoId(dictionary, 12)
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }
}
