package com.quickgame.circlechallenge.domain.repository

import com.quickgame.circlechallenge.data.datastore.DataStore
import com.quickgame.circlechallenge.data.repository.IRepository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val dataStore: DataStore,
) : IRepository {
    override fun getBestScore(): Flow<Int> = dataStore.highScore
    override suspend fun saveBestScore(score: Int) = dataStore.storeHighScore(score)
    override fun getAvatarId(): Flow<Int> = dataStore.avatarId

    override suspend fun setAvatarId(id: Int) = dataStore.setAvatarId(id)
    override fun getIsPlayingSound(): Flow<Boolean> = dataStore.isPlayingSound

    override suspend fun setIsPlayingSound(isPlayingSound: Boolean) = dataStore.setIsPlayingSound(isPlayingSound)
}