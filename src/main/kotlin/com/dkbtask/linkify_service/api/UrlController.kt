package com.dkbtask.linkify_service.api

import com.dkbtask.linkify_service.dto.LongUrlResponse
import com.dkbtask.linkify_service.dto.ShortUrlResponse
import com.dkbtask.linkify_service.dto.UrlRequest
import com.dkbtask.linkify_service.exception.ErrorResponse
import com.dkbtask.linkify_service.model.toLongUrlResponse
import com.dkbtask.linkify_service.model.toShortUrlResponse
import com.dkbtask.linkify_service.service.UrlService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/url")
class UrlController(
    private val urlService: UrlService,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }


    @Operation(
        summary = "Shorten a URL",
        description = "Takes a long URL as input and returns a shortened identifier."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL successfully shortened",
                content = [Content(schema = Schema(implementation = ShortUrlResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid link is supplied.",
                content = [Content(
                    schema = Schema(implementation = ErrorResponse::class),
                    examples = [
                        ExampleObject(
                            value = """
                                {
                                    "errorCode": "INVALID_LINK_PROVIDED",
                                    "errorDescription": "The provided link is missing or invalid."
                                }
                                """
                        )
                    ]
                )]
            ),
        ]
    )
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody urlRequest: UrlRequest): ResponseEntity<ShortUrlResponse> {
        logger.info { "received shortening URL request. $urlRequest" }
        val urlResponse = urlService.saveUrl(urlRequest)
        return ResponseEntity.ok(urlResponse.toShortUrlResponse())
    }


    @Operation(
        summary = "Resolve a shortened URL to produce the full (Long) url.",
        description = "Takes a shortenedUrlIdentifier and to retrieve the original long URL."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "URL successfully resolved",
                content = [Content(schema = Schema(implementation = LongUrlResponse::class),
                    examples = [
                        ExampleObject(
                            name = "Shortened URL identifier",
                            summary = "Sample shortened URL response",
                            value = """
                            {
                                "shortUrlIdentifier": "aAwhOxAn_Y4U"
                            }
                            """
                        )
                    ])
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Short URL not found",
                content = [Content(
                    schema = Schema(implementation = ErrorResponse::class),
                    examples = [
                        ExampleObject(
                            value = """
                                {
                                    "errorCode": "SHORT_URL_NOT_FOUND",
                                    "errorDescription": "The requested short URL could not be found."
                                }
                                """
                        )
                    ])
                ]
            )
        ]
    )
    @GetMapping("/resolve/{shortUrl}")
    fun resolveUrl(@PathVariable shortUrl: String): ResponseEntity<LongUrlResponse> {
        logger.info { "received resolve shortened URL request. $shortUrl" }
        val urlResponse = urlService.fetchLongUrl(shortUrl)
        return ResponseEntity.ok(urlResponse.toLongUrlResponse())
    }
}
