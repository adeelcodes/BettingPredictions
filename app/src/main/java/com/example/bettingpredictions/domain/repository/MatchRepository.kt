package com.example.bettingpredictions.domain.repository

import com.example.bettingpredictions.domain.model.MatchesData

interface MatchRepository {
    fun getMatchesFromRoom(): List<MatchesData>

    fun getMatchesByTeamNamesFromRoom(team1: String, team2: String): MatchesData

    fun addMatchesToRoom(matches: List<MatchesData>)

    fun updateMatchesInRoom(matches: List<MatchesData>)

    fun updateMatchInRoom(match: MatchesData)

    fun deleteMatchFromRoom(match: MatchesData)
    fun restoreRoom()
}