package com.dkbtask.linkify_service.exception

data class ShortUrlNotFoundException(
    override val message: String? = null,
    val errorCode: ErrorCode = ErrorCode.URL_NOT_FOUND
) : RuntimeException(message)

data class InvalidLinkException(
    override val message: String? = null,
    val errorCode: ErrorCode = ErrorCode.BAD_REQUEST
) : RuntimeException(message)

enum class ErrorCode() {
    URL_NOT_FOUND,
    BAD_REQUEST ,
    SHORT_URL_ALREADY_EXISTS
}
