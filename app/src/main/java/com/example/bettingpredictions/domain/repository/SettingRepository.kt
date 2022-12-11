package com.example.bettingpredictions.domain.repository

import com.example.bettingpredictions.domain.model.SettingData


interface SettingRepository {
    fun getSettingList(): List<SettingData>
    fun addData(data: SettingData)
    fun updateData(data: SettingData)
}