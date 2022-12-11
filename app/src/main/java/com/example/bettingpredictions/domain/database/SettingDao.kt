package com.example.bettingpredictions.domain.database

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.example.bettingpredictions.domain.model.SettingData
import com.example.bettingpredictions.util.Constants

@Dao
interface SettingDao {
    @Query("SELECT * FROM ${Constants.SETTING_TABLE} ORDER BY id ASC")
    fun getSettingList(): List<SettingData>

    @Insert(onConflict = IGNORE)
    fun addData(data: SettingData)

    @Update
    fun updateData(data: SettingData)
}