package dev.gressier.roomsample.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity
data class Message(
   @PrimaryKey
    val messageId: String,
    val content: String,
    val userId: String,
)