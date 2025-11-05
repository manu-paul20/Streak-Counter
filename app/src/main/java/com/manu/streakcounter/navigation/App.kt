package com.manu.streakcounter.navigation

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.manu.streakcounter.database.Streak
import com.manu.streakcounter.main_ui.Home
import com.manu.streakcounter.main_ui.Start
import com.manu.streakcounter.main_ui.StreakDetails

@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val isAppLockEnabled = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE).getBoolean("isAppLockEnabled",false)
    NavHost(
        navController = navController,
        startDestination = Routes.Start,
        enterTransition = {
            slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = { it }
            )
        }
    ) {
        composable<Routes.Home> {
            Home(navController = navController)
        }

        composable<Routes.DetailsScreen> {
            val data = it.toRoute<Routes.DetailsScreen>()
            StreakDetails(
                streak = Streak(
                    streakName = data.streakName,
                    currentStreak = data.streakCount,
                    lastUpdateTime = data.lastUpdateTime,
                    targetStreak = data.targetStreak,
                    lastIncreasePressDate = data.lastIncreasePressTime
                    ),
                navController = navController
            )
        }

        composable<Routes.Start> { Start(navController) }
    }
}