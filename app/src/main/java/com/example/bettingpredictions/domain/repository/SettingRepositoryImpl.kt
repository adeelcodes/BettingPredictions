package com.example.bettingpredictions.domain.repository

import com.example.bettingpredictions.domain.database.SettingDao
import com.example.bettingpredictions.domain.model.SettingData


class SettingRepositoryImpl(
    private val settingDao: SettingDao,
) : SettingRepository {

    override fun getSettingList(): List<SettingData> {
        return settingDao.getSettingList()
    }

    override fun addData(data: SettingData) {
        return settingDao.addData(data)
    }

    override fun updateData(data: SettingData) {
        return settingDao.updateData(data)
    }

}