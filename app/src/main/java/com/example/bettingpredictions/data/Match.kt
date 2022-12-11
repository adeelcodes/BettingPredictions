package com.example.bettingpredictions.data

data class Match(
    val team1: String,
    val team2: String,
    val team1_points: Int,
    val team1_bet_points: Int = 0,
    val team2_points: Int,
    val team2_bet_points: Int = 0,
)