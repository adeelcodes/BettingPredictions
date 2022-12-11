package com.example.bettingpredictions.domain.api

import com.example.bettingpredictions.data.MatchResponse
import com.example.bettingpredictions.data.MatchResultResponse
import retrofit2.http.GET


interface ApiService {

    @GET(ApiConstants.MATCH_END_POINT)
    suspend fun getMatches(): MatchResponse

    @GET(ApiConstants.RESULT_END_POINT)
    suspend fun getResults(): MatchResultResponse
}