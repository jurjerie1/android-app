package dev.gressier.roomsample.data.Dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto (
    val email: String,
    val password: String
)