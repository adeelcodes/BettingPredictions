package com.example.bettingpredictions.presentation.result.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bettingpredictions.domain.model.MatchesData
import com.example.bettingpredictions.domain.model.SettingData
import com.example.bettingpredictions.domain.repository.ApiRepo
import com.example.bettingpredictions.domain.repository.MatchRepository
import com.example.bettingpredictions.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.example.bettingpredictions.data.MatchResult

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val apiRepo: ApiRepo,
    private val matchRepo: MatchRepository,
    private val settingRepo: SettingRepository,
) : ViewModel() {
    val loadApi = mutableStateOf(false)

    val _matchList = MutableStateFlow(ArrayList<MatchResult>())
    val matchList: StateFlow<ArrayList<MatchResult>> get() = _matchList

    val _matchDataList = MutableStateFlow(ArrayList<MatchesData>())
    val matchDataList: StateFlow<ArrayList<MatchesData>> get() = _matchDataList

    fun getMatchData() {
        Thread {
            _matchDataList.value = ArrayList(matchRepo.getMatchesFromRoom())
        }.start()
    }

    fun getResulData() {
        Thread {
            var settingList = settingRepo.getSettingList()
            var isApiCall = false
            if (settingList.isNotEmpty()) {
                var settingData = settingList[0]
                var matchApiTime = settingData.resultsApiTime
                if (matchApiTime == 0L) isApiCall = true
                else {
                    var currentTime = Calendar.getInstance().timeInMillis

                    val difference: Long = currentTime - matchApiTime
                    var days = (difference / (1000 * 60 * 60 * 24))
                    var hours = ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))
                    var min =
                        (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60)
                    Log.e("getMatchData", " Time $hours:$min")
                    if (min >= 1)
                        isApiCall = true
                }
            } else
                isApiCall = true
            kotlin.run {
                if (isApiCall)
                    resultApi()
                else {
                    getMatchData()
                    setScreenOpen()
                }
            }
        }.start()
    }


    fun resultApi() {
        loadApi.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                apiRepo.getResultsFromApi()
            }.fold({
                Thread {
                    setSettingTime()
                    _matchList.value = it.matchResults
                    var addlList: ArrayList<MatchesData> = ArrayList()
                    var updateList: ArrayList<MatchesData> = ArrayList()
                    for (match in it.matchResults) {
                        var data = matchRepo.getMatchesByTeamNamesFromRoom(match.team1, match.team2)
                        if (data != null) {
                            updateList.add(
                                MatchesData(
                                    data.id,
                                    data.team1,
                                    data.team2,
                                    match.team1_points,
                                    data.team1_bet_points,
                                    match.team2_points,
                                    data.team2_bet_points
                                )
                            )
                        } else {
                            addlList.add(
                                MatchesData(
                                    0,
                                    match.team1,
                                    match.team2,
                                    match.team1_points,
                                    match.team1_bet_points,
                                    match.team2_points,
                                    match.team2_bet_points
                                )
                            )
                        }
                    }

                    updateMatches(updateList)
                    addMatches(addlList)
                    kotlin.run {
                        getMatchData()
                    }
                }.start()

            }, {
                if (it is HttpException) {
                    if (it.code() == 404 || it.code() == 422) {
                        // TODO
                    }
                }
            })
            loadApi.value = false
        }
    }

    private fun setScreenOpen() {
        Thread {
            var settingList = settingRepo.getSettingList()
            if (settingList.isNotEmpty()) {
                var settingData = settingList[0]
                settingData.screenNum = 1
                settingRepo.updateData(settingData)
            } else {
                settingRepo.addData(
                    SettingData(
                        id = 0,
                        matchApiTime = 0,
                        resultsApiTime = 0,
                        screenNum = 1
                    )
                )
            }
        }.start()
    }

    private fun setSettingTime() {
        Thread {
            var currentTime = Calendar.getInstance().timeInMillis
            var settingList = settingRepo.getSettingList()
            if (settingList.isNotEmpty()) {
                var settingData = settingList[0]
                settingData.resultsApiTime = currentTime
                settingRepo.updateData(settingData)
            } else {
                settingRepo.addData(
                    SettingData(
                        id = 0,
                        matchApiTime = 0,
                        resultsApiTime = currentTime,
                        screenNum = 1
                    )
                )
            }
        }.start()
    }


    fun addMatches(matchesData: ArrayList<MatchesData>) = viewModelScope.launch(Dispatchers.IO) {
        matchRepo.addMatchesToRoom(matchesData)
    }

    fun updateMatches(matchesData: ArrayList<MatchesData>) = viewModelScope.launch(Dispatchers.IO) {
        matchRepo.updateMatchesInRoom(matchesData)
    }

    fun restoreData() {
        Thread {
            matchRepo.restoreRoom()
        }.start()
    }
}