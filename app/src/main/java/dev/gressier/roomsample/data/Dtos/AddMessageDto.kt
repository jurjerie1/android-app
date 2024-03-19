package dev.gressier.roomsample.data.Dtos

import kotlinx.serialization.Serializable

@Serializable
data class AddMessageDto (
    val content : String
)