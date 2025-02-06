package com.dkbtask.linkify_service.model

import com.dkbtask.linkify_service.dto.LongUrlResponse
import com.dkbtask.linkify_service.dto.ShortUrlResponse
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
@Entity
data class Url(
    @Id
    val shortUrl: String,
    val longUrl: String,
    val createdAt: Instant = Instant.now(),
) {
    // hibernate requires no-arg constructor
    constructor() : this("", "", Instant.now())
}


fun Url.toShortUrlResponse(): ShortUrlResponse = ShortUrlResponse(shortUrl)
fun Url.toLongUrlResponse(): LongUrlResponse = LongUrlResponse(longUrl)