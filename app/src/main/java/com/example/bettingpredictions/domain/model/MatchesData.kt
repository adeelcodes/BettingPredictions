package com.example.bettingpredictions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bettingpredictions.util.Constants.Companion.MATCHES_TABLE

@Entity(tableName = MATCHES_TABLE)
data class MatchesData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var team1: String,
    var team2: String,
    var team1_points: Int =0,
    var team1_bet_points: Int = 0,
    var team2_points: Int=0,
    var team2_bet_points: Int = 0
)