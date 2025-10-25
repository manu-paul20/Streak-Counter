package com.manu.streakcounter.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object Home

    @Serializable
    object Start

    @Serializable
    data class DetailsScreen(
        val streakName: String,
        val streakCount: Int,
        val lastUpdateTime: String,
        val targetStreak : Int
    )
}