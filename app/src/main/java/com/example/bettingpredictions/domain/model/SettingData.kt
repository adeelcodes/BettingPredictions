package com.example.bettingpredictions.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bettingpredictions.util.Constants.Companion.SETTING_TABLE

@Entity(tableName = SETTING_TABLE)
data class SettingData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var matchApiTime: Long = 0,
    var resultsApiTime: Long = 0,
    var screenNum: Int = 0,
)