package com.inventrohyder.will

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WillDao {
    @Query("SELECT * FROM will_table")
    fun getWills(): Flow<List<Will>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(will: Will)

    @Query("DELETE FROM will_table")
    suspend fun deleteAll()
}