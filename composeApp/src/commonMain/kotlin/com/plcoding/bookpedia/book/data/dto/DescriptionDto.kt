package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DescriptionDto(
    @SerialName("value") val value: String,
    @SerialName("type") val type: String,
)
