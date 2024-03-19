package dev.gressier.roomsample.data.local

import androidx.room.TypeConverter
import java.util.UUID

class UUIDTypeConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuidString: String): UUID = UUID.fromString(uuidString)
}