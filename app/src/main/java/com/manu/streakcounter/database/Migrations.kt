package com.manu.streakcounter.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

fun Migration_1_2() : Migration = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE StreakTable ADD COLUMN lastUpdateTime TEXT NOT NULL DEFAULT '00/00/0000'")
    }
}

fun Migration_2_3() = object : Migration(2,3){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE streakTable ADD COLUMN targetStreak INTEGER NOT NULL DEFAULT -1")
    }
}

fun Migration_3_4() = object : Migration(3,4){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE streakTable ADD COLUMN lastIncreasePressDate TEXT NOT NULL DEFAULT 'NA'")
    }
}