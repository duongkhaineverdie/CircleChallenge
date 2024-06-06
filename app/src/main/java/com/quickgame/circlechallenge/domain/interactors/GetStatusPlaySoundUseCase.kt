package com.quickgame.circlechallenge.domain.interactors

import com.quickgame.circlechallenge.data.repository.IRepository
import com.quickgame.circlechallenge.domain.interactors.type.TrueColorBaseUseCaseFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GetStatusPlaySoundUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : TrueColorBaseUseCaseFlow<Unit, Boolean>(dispatcher) {
    override suspend fun build(param: Unit): Flow<Boolean> = repository.getIsPlayingSound()

}