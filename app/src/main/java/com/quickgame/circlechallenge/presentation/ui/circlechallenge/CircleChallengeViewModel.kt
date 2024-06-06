package com.quickgame.circlechallenge.presentation.ui.circlechallenge

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickgame.circlechallenge.data.soundmanager.SoundManager
import com.quickgame.circlechallenge.domain.interactors.GetBestScoreUseCase
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import com.quickgame.circlechallenge.domain.interactors.SaveBestScoreUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CircleChallengeViewModel(
    savedStateHandle: SavedStateHandle,
    private val getStatusPlaySoundUseCase: GetStatusPlaySoundUseCase,
    private val soundManager: SoundManager,
    private val getHighScoreUseCase: GetBestScoreUseCase,
    private val saveHighScoreUseCase: SaveBestScoreUseCase,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<CircleChallengeState> =
        MutableStateFlow(CircleChallengeState())

    val stateFlow: StateFlow<CircleChallengeState> = _stateFlow.asStateFlow()

    init {
        getStatusPlayingSound()
        getBestScore()
    }

    fun onDefeat(score: Int) {
        _stateFlow.update {
            it.copy(
                score = score,
                isDefeat = true
            )
        }
        if (stateFlow.value.bestScore < score) {
            saveBestScore(score)
        }
    }

    private fun getStatusPlayingSound() {
        viewModelScope.launch {
            getStatusPlaySoundUseCase(Unit).collectLatest { result ->
                result.onSuccess { value ->
                    _stateFlow.update {
                        it.copy(
                            isPlayingSoundClick = value
                        )
                    }
                }
            }
        }
    }

    private fun getBestScore() {
        viewModelScope.launch {
            getHighScoreUseCase(Unit).collectLatest { result ->
                result.onSuccess { value ->
                    _stateFlow.update {
                        it.copy(
                            bestScore = value
                        )
                    }
                }
            }
        }
    }

    private fun saveBestScore(score: Int) {
        viewModelScope.launch {
            saveHighScoreUseCase(score).onSuccess {
                getBestScore()
            }
        }
    }

    fun playSoundClick() {
        if (stateFlow.value.isPlayingSoundClick) {
            soundManager.playSound()
        }
    }

    fun playSoundLose() {
        if (stateFlow.value.isPlayingSoundClick) {
            soundManager.playSoundLose()
        }
    }

    fun onRestart() {
        _stateFlow.update {
            it.copy(
                isDefeat = false,
            )
        }
    }

}

data class CircleChallengeState(
    val selectedColor: Int = 0,
    val isDefeat: Boolean = false,
    val score: Int = 0,
    val isPlayingSoundClick: Boolean = false,
    val bestScore: Int = 0,
)