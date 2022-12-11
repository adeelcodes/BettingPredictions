package com.example.bettingpredictions.data

data class MatchResult(
    var team1: String,
    var team1_points: Int,
    var team1_bet_points: Int,
    var team2: String,
    var team2_points: Int,
    var team2_bet_points: Int,
)