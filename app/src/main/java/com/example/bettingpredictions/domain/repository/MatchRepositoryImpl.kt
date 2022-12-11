package com.example.bettingpredictions.domain.repository

import com.example.bettingpredictions.domain.database.MatchDao
import com.example.bettingpredictions.domain.model.MatchesData


class MatchRepositoryImpl(
    private val matchDao: MatchDao,
) : MatchRepository {
    override fun getMatchesFromRoom(): List<MatchesData> {
        return matchDao.getMatchList()
    }

    override fun getMatchesByTeamNamesFromRoom(team1: String, team2: String): MatchesData {
        return matchDao.getMatchesByTeamNames(team1, team2)
    }

    override fun addMatchesToRoom(matches: List<MatchesData>) {
        return matchDao.addMatches(matches)
    }

    override fun updateMatchesInRoom(matches: List<MatchesData>) {
        return matchDao.updateMatchList(matches)
    }

    override fun updateMatchInRoom(match: MatchesData) = matchDao.updateMatch(match)

    override fun deleteMatchFromRoom(match: MatchesData) = matchDao.deleteMatch(match)

    override fun restoreRoom() {
        matchDao.restoreRoom()
    }

}