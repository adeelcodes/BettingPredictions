package com.example.bettingpredictions.di

import android.content.Context
import androidx.room.Room
import com.example.bettingpredictions.BuildConfig
import com.example.bettingpredictions.domain.api.ApiConstants
import com.example.bettingpredictions.domain.api.ApiService
import com.example.bettingpredictions.domain.database.MatchDao
import com.example.bettingpredictions.domain.database.MatchDb
import com.example.bettingpredictions.domain.database.SettingDao
import com.example.bettingpredictions.domain.repository.MatchRepository
import com.example.bettingpredictions.domain.repository.MatchRepositoryImpl
import com.example.bettingpredictions.domain.repository.SettingRepository
import com.example.bettingpredictions.domain.repository.SettingRepositoryImpl
import com.example.bettingpredictions.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): ApiService {
        return builder
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .cookieJar(MyCookieJar())
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    fun provideMatchDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        MatchDb::class.java,
        Constants.MATCHES_TABLE
    ).build()

    @Provides
    fun provideMatchDao(
        matchDb: MatchDb
    ) = matchDb.matchDao()

    @Provides
    fun provideMatchRepository(
        matchDao: MatchDao
    ): MatchRepository = MatchRepositoryImpl(
        matchDao = matchDao
    )

    @Provides
    fun provideSettingDao(
        matchDb: MatchDb
    ) = matchDb.settingDao()

    @Provides
    fun provideSettingRepository(
        settingDao: SettingDao
    ): SettingRepository = SettingRepositoryImpl(
        settingDao = settingDao
    )
}