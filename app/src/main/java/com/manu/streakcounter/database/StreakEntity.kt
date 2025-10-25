package com.manu.streakcounter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StreakTable")
data class Streak(
    @PrimaryKey val streakName: String,
    val currentStreak : Int,
    // changes in V2
    val lastUpdateTime : String, // <- added this in V2

    // changes in V3
    val targetStreak: Int
)