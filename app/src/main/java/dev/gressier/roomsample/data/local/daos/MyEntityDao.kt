package dev.gressier.roomsample.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.gressier.roomsample.data.local.entities.MyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyEntityDao {
    @Insert
    suspend fun insert(myEntity: MyEntity)

    @Query("SELECT * FROM MyEntity")
    fun getAllEntities(): Flow<List<MyEntity>>
}