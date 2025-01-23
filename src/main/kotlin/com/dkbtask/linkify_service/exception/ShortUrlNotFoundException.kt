package com.dkbtask.linkify_service.exception

data class ShortUrlNotFoundException(
    override val message: String? = null
): RuntimeException(ErrorCode.URL_NOT_FOUND.name +": $message")

data class BadRequestException(
    override val message: String? = null
): RuntimeException(ErrorCode.BAD_REQUEST.name +": $message")


enum class ErrorCode {
    URL_NOT_FOUND,
    BAD_REQUEST,
    SHORT_URL_ALREADY_EXISTS
}
