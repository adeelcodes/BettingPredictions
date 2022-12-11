package com.example.bettingpredictions.domain.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.example.bettingpredictions.domain.model.MatchesData
import com.example.bettingpredictions.util.Constants

@Dao
interface MatchDao {
    @Query("SELECT * FROM ${Constants.MATCHES_TABLE} ORDER BY id ASC")
    fun getMatchList(): List<MatchesData>

    @Query("SELECT * FROM ${Constants.MATCHES_TABLE} WHERE id = :id")
    fun getMatchById(id: Int): MatchesData

    @Query("SELECT * FROM ${Constants.MATCHES_TABLE} WHERE team1 = :team1 AND team2 = :team2")
    fun getMatchesByTeamNames(team1: String, team2: String): MatchesData

    @Insert(onConflict = IGNORE)
    fun addMatches(matches: List<MatchesData>)

    @Update
    fun updateMatch(match: MatchesData)

    @Update
    fun updateMatchList(matches: List<MatchesData>)

    @Query("UPDATE ${Constants.MATCHES_TABLE} SET team1_bet_points = 0 ,team2_bet_points = 0 ")
    fun restoreRoom()

    @Delete
    fun deleteMatch(match: MatchesData)
}