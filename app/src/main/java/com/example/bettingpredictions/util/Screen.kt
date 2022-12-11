package com.example.bettingpredictions.util

sealed class Screen(val route: String) {
    object HomeScreenRoute : Screen("home_screen")
    object ResultScreenRoute : Screen("result_screen")
}