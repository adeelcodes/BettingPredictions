package com.example.bettingpredictions.presentation.home.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bettingpredictions.data.Match
import com.example.bettingpredictions.domain.model.MatchesData
import com.example.bettingpredictions.domain.model.SettingData
import com.example.bettingpredictions.domain.repository.ApiRepo
import com.example.bettingpredictions.domain.repository.MatchRepository
import com.example.bettingpredictions.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepo: ApiRepo,
    private val matchRepo: MatchRepository,
    private val settingRepo: SettingRepository,
) : ViewModel() {
    val loadApi = mutableStateOf(false)
    var openScreenPos = mutableStateOf(0)
    var isApiCalled = false

    val _matchDataList = MutableStateFlow(ArrayList<MatchesData>())
    val matchDataList: StateFlow<ArrayList<MatchesData>> get() = _matchDataList

    fun getScreenOpenPos() {
        Thread {
            val settingList = settingRepo.getSettingList()
            if (settingList.isNotEmpty()) {
                val settingData = settingList[0]
                openScreenPos.value = settingData.screenNum
            }
        }.start()
    }

    fun getMatchData() {
        Thread {
            setScreenOpen()
            _matchDataList.value = ArrayList(matchRepo.getMatchesFromRoom())
            val settingList = settingRepo.getSettingList()
            var isApiCall = false
            if (settingList.isNotEmpty()) {
                val settingData = settingList[0]
                val matchApiTime = settingData.matchApiTime
                if (matchApiTime == 0L) isApiCall = true
                else {
                    val currentTime = Calendar.getInstance().timeInMillis

                    val difference: Long = currentTime - matchApiTime
                    val days = (difference / (1000 * 60 * 60 * 24))
                    val hours = ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))
                    val min =
                        (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60)
                    Log.e("getMatchData", " Time $hours:$min")
                    if (min >= 1)
                        isApiCall = true
                }
            } else
                isApiCall = true


            kotlin.run {
                if (!loadApi.value && !isApiCalled && isApiCall)
                    matchApi()
                else {
                    loadApi.value = false
                }
            }
        }.start()
    }


    private fun setScreenOpen() {
        Thread {
            val settingList = settingRepo.getSettingList()
            if (settingList.isNotEmpty()) {
                val settingData = settingList[0]
                settingData.screenNum = 0
                settingRepo.updateData(settingData)
            } else {
                settingRepo.addData(
                    SettingData(
                        id = 0,
                        matchApiTime = 0,
                        resultsApiTime = 0,
                        screenNum = 0
                    )
                )
            }
        }.start()
    }

    private fun matchApi() {
        loadApi.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                apiRepo.getMatchesFromApi()
            }.fold({
                val list = it.matches
                isApiCalled = true
                setSettingTime()
                val addList: ArrayList<MatchesData> = ArrayList()
                Thread {
                    for (match in list) {
                        val data = matchRepo.getMatchesByTeamNamesFromRoom(match.team1, match.team2)
                        if (data == null)
                            addList.add(
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
                    addMatches(addList)

                    kotlin.run {
                        _matchDataList.value = ArrayList(matchRepo.getMatchesFromRoom())
                        findRemoveList(list)
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

    private fun findRemoveList(matchList: ArrayList<Match>) {
        val removeList: ArrayList<MatchesData> = ArrayList()
        for (matchesData in matchDataList.value) {
            var isNotFind = true
            for (match in matchList) {
                if (matchesData.team1 == match.team1 && matchesData.team2 == match.team2) {
                    isNotFind = false
                    break
                }
            }
            if (isNotFind)
                removeList.add(matchesData)
        }
        if (removeList.isNotEmpty()) {

            for (matchesData in removeList) {
                matchRepo.deleteMatchFromRoom(matchesData)
            }
            Thread {
                _matchDataList.value = ArrayList(matchRepo.getMatchesFromRoom())
            }.start()
        }
    }

    private fun setSettingTime() {
        Thread {
            val currentTime = Calendar.getInstance().timeInMillis
            val settingList = settingRepo.getSettingList()
            if (settingList.isNotEmpty()) {
                val settingData = settingList[0]
                settingData.matchApiTime = currentTime
                settingData.screenNum = 0
                settingRepo.updateData(settingData)
            } else {
                settingRepo.addData(
                    SettingData(
                        id = 0,
                        matchApiTime = currentTime,
                        resultsApiTime = 0,
                        screenNum = 0
                    )
                )
            }
        }.start()
    }

    private fun addMatches(matchesData: ArrayList<MatchesData>) =
        viewModelScope.launch(Dispatchers.IO) {
            matchRepo.addMatchesToRoom(matchesData)
            _matchDataList.value = ArrayList(matchRepo.getMatchesFromRoom())
        }

    fun updateMatch(matchesData: MatchesData) = viewModelScope.launch(Dispatchers.IO) {
        matchRepo.updateMatchInRoom(matchesData)
        getMatchData()
    }
}