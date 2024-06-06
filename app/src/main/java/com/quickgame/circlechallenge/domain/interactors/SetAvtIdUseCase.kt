package com.quickgame.circlechallenge.domain.interactors

import com.quickgame.circlechallenge.data.repository.IRepository
import com.quickgame.circlechallenge.domain.interactors.type.TrueColorBaseUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SetAvtIdUseCase(
    private val repository: IRepository,
    dispatcher: CoroutineDispatcher,
) : TrueColorBaseUseCase<Int, Unit>(dispatcher) {
    override suspend fun block(param: Int): Unit = repository.setAvatarId(id = param)
}