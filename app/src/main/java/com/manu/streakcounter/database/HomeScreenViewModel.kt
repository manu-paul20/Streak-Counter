package com.manu.streakcounter.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application): AndroidViewModel(application) {
    val streakDao = AppDB.getInstance(application).streakDao()
    val streakList = streakDao.getAllStreaks()

    fun updateStreak(streak: Streak){
        viewModelScope.launch {
            streakDao.updateStreak(streak)
        }
    }
    fun addStreak(streak: Streak){
        viewModelScope.launch {
            streakDao.addStreak(streak)
        }
    }
    fun deleteStreak(streak: Streak){
        viewModelScope.launch {
            streakDao.deleteStreak(streak)
        }
    }
}