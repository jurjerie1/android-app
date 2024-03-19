package dev.gressier.roomsample.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.gressier.roomsample.data.local.entities.Message
import dev.gressier.roomsample.data.local.entities.MyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)

//    @Query("SELECT * FROM Message")
//    fun getAllEntities(): Flow<List<message>>
}