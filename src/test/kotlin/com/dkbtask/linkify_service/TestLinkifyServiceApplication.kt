package com.dkbtask.linkify_service

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<LinkifyServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
