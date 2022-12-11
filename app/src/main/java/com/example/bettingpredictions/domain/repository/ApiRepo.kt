package com.example.bettingpredictions.domain.repository

import com.example.bettingpredictions.data.MatchResponse
import com.example.bettingpredictions.data.MatchResultResponse
import com.example.bettingpredictions.domain.api.ApiService
import javax.inject.Inject

class ApiRepo @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getMatchesFromApi(): MatchResponse {
        return apiService.getMatches()
    }

    suspend fun getResultsFromApi(): MatchResultResponse {
        return apiService.getResults()
    }
}