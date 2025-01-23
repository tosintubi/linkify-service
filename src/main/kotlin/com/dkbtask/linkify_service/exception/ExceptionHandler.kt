package com.dkbtask.linkify_service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver

@ControllerAdvice
class ExceptionHandler: ResponseStatusExceptionResolver() {

    @ExceptionHandler(ShortUrlNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleShortUrlNotFoundException(exception: ShortUrlNotFoundException): ErrorResponse {
        logger.warn { " ⚠️ ShortUrl not found: $exception" } // Should log be WARN or ERROR
        return ErrorResponse("SHORT_URL_NOT_FOUND", HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(exception: BadRequestException): ErrorResponse {
        logger.warn { " ⚠️ Missing : $exception" }
        return ErrorResponse("MISSING_SHORTLINK_IN_REQUEST", HttpStatus.BAD_REQUEST)
    }
}

data class ErrorResponse(
    val errorDetail: String?,
    val errorCode: HttpStatusCode
)