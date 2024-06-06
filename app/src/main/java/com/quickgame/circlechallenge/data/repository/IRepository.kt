package com.quickgame.circlechallenge.data.repository

import kotlinx.coroutines.flow.Flow

interface IRepository {
    fun getBestScore(): Flow<Int>
    suspend fun saveBestScore(score: Int)
    fun getAvatarId(): Flow<Int>
    suspend fun setAvatarId(id: Int)

    fun getIsPlayingSound(): Flow<Boolean>
    suspend fun setIsPlayingSound(isPlayingSound: Boolean)
}