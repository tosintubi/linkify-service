package com.dkbtask.linkify_service.api

import com.dkbtask.linkify_service.dto.LongUrlResponse
import com.dkbtask.linkify_service.dto.ShortUrlResponse
import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.model.toLongUrlResponse
import com.dkbtask.linkify_service.model.toShortUrlResponse
import com.dkbtask.linkify_service.service.UrlService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/url-service")
class UrlController(
    private val urlService: UrlService,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody urlRequest: UrlRequest): ResponseEntity<ShortUrlResponse> {
        logger.info { "received shortening URL request. $urlRequest" }
        val urlResponse = urlService.saveUrl(urlRequest)
        return ResponseEntity.ok(urlResponse.toShortUrlResponse())
    }

    @GetMapping("/resolve/{shortUrl}")
    fun resolveUrl(@PathVariable shortUrl: String): ResponseEntity<LongUrlResponse> {
        logger.info { "received resolve shortened URL request. $shortUrl" }
        val urlResponse = urlService.fetchLongUrl(shortUrl)
        return ResponseEntity.ok(urlResponse.toLongUrlResponse())
    }
}
