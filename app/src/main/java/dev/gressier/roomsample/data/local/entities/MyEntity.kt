package dev.gressier.roomsample.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class MyEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
)