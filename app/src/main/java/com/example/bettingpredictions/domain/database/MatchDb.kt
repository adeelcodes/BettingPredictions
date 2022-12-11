package com.example.bettingpredictions.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bettingpredictions.domain.model.MatchesData
import com.example.bettingpredictions.domain.model.SettingData

@Database(entities = [MatchesData::class, SettingData::class], version = 1, exportSchema = false)
abstract class MatchDb : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun settingDao(): SettingDao
}