package com.free4developer.sampleserver.domain.oauth2.repository

import com.free4developer.sampleserver.domain.oauth2.entity.OAuth2Attributes
import org.springframework.data.repository.CrudRepository

interface OAuth2Repository : CrudRepository<OAuth2Attributes, Long> {
}