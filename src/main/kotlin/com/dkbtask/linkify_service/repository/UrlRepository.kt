package com.dkbtask.linkify_service.repository

import com.dkbtask.linkify_service.model.Url
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlRepository: JpaRepository<Url, String>