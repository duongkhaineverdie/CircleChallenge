package com.quickgame.circlechallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickgame.circlechallenge.data.mediaplayer.MediaPlayerManager
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val playerManager: MediaPlayerManager,
    private val getStatusPlaySoundUseCase: GetStatusPlaySoundUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())

    init {
        getStatusPlayingSound()
    }
    private fun getStatusPlayingSound() {
        viewModelScope.launch {
            getStatusPlaySoundUseCase(Unit).collectLatest { result ->
                result.onSuccess { value ->
                    _uiState.update {
                        it.copy(
                            isPlayingSoundClick = value
                        )
                    }
                }
            }
        }
    }
    fun onPause(){
        if (_uiState.value.isPlayingSoundClick) {
            playerManager.pauseMusic()
        }
    }
    fun onResume(){
        if (_uiState.value.isPlayingSoundClick) {
            playerManager.resumeMusic()
        }
    }
}

data class MainUiState(
    val isPlayingSoundClick: Boolean = true,
)