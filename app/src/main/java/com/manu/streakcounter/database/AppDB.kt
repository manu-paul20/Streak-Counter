package com.manu.streakcounter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Streak::class], version = 2, exportSchema = false)
abstract class AppDB(): RoomDatabase(){
    abstract fun streakDao(): StreakDao

    companion object{
       private var Instance: AppDB? = null

        fun getInstance(context: Context): AppDB{
            return Instance?:synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDB::class.java,
                    name = "app_db"
                ).addMigrations(Migration_1_2())
                    .build()
                Instance = instance
                instance
            }
        }
    }
}