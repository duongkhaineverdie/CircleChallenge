package com.quickgame.circlechallenge.domain.interactors

import com.quickgame.circlechallenge.data.repository.IRepository
import com.quickgame.circlechallenge.domain.interactors.type.TrueColorBaseUseCaseFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GetBestScoreUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : TrueColorBaseUseCaseFlow<Unit, Int>(dispatcher) {
    override suspend fun build(param: Unit): Flow<Int> = repository.getBestScore()

}