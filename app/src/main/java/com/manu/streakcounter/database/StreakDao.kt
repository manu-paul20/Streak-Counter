package com.manu.streakcounter.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao{

    @Query("select * from Streaktable")
    fun getAllStreaks(): Flow<List<Streak>>

    @Update
   suspend fun updateStreak(streak: Streak)

   @Delete
   suspend fun deleteStreak(streak: Streak)

    @Insert
    suspend fun addStreak(streak: Streak)
}