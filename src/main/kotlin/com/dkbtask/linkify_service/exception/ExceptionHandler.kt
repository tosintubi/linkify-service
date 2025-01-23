package com.dkbtask.linkify_service.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @ExceptionHandler(ShortUrlNotFoundException::class)
    fun handleShortUrlNotFoundException(exception: ShortUrlNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn { " ⚠️ ShortUrl not found: $exception" }
        return ResponseEntity(
            ErrorResponse(
                errorDescription = "The requested short URL could not be found.",
                errorCode = "SHORT_URL_NOT_FOUND",
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(InvalidLinkException::class)
    fun handleBadRequestException(exception: InvalidLinkException): ResponseEntity<ErrorResponse> {
        logger.warn { " ‼️ Invalid link in request : $exception" }
        return ResponseEntity(
            ErrorResponse(
                errorDescription = "The provided link is missing or invalid.",
                errorCode = "INVALID_LINK_PROVIDED",
            ),
            HttpStatus.BAD_REQUEST
        )
    }
}

data class ErrorResponse(
    val errorDescription: String,
    val errorCode: String,
)