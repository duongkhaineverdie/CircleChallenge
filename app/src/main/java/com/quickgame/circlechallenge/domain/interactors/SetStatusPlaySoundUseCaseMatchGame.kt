package com.quickgame.circlechallenge.domain.interactors

import com.quickgame.circlechallenge.data.repository.IRepository
import com.quickgame.circlechallenge.domain.interactors.type.TrueColorBaseUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SetStatusPlaySoundUseCaseMatchGame(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : TrueColorBaseUseCase<Boolean, Unit>(dispatcher) {
    override suspend fun block(param: Boolean): Unit = repository.setIsPlayingSound(isPlayingSound = param)
}