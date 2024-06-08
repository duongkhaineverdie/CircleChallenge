package com.quickgame.circlechallenge.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.data.mediaplayer.MediaPlayerManager
import com.quickgame.circlechallenge.data.soundmanager.SoundManager
import com.quickgame.circlechallenge.domain.interactors.GetAvtIdUseCase
import com.quickgame.circlechallenge.domain.interactors.GetBestScoreUseCase
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import com.quickgame.circlechallenge.domain.interactors.SetAvtIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    val getBestScoreUseCase: GetBestScoreUseCase,
    private val getAvtIdUseCase: GetAvtIdUseCase,
    private val setAvtIdUseCase: SetAvtIdUseCase,
    private val getStatusPlaySoundUseCase: GetStatusPlaySoundUseCase,
    private val soundManager: SoundManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getStatusPlayingSound()
        getAvatarId()
        viewModelScope.launch {
            getBestScoreUseCase(Unit).collectLatest {
                it.onSuccess {highScore ->
                    _uiState.update { state ->
                        state.copy(
                            bestScore = highScore
                        )
                    }
                }
            }
        }
    }

    fun setAvatarId(id: Int) {
        viewModelScope.launch {
            setAvtIdUseCase(id).onSuccess {
                getAvatarId()
            }
        }
    }

    private fun getAvatarId() {
        viewModelScope.launch {
            getAvtIdUseCase(Unit).collectLatest { result ->
                result.onSuccess { value ->
                    _uiState.update {
                        it.copy(
                            avatarId = value,
                            isShowDialogChooseAvatar = false,
                        )
                    }
                }
            }
        }
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

    fun showDialogChooseAvatar() {
        _uiState.update {
            it.copy(
                isShowDialogChooseAvatar = true
            )
        }
    }

    fun playSoundClick() {
        if (uiState.value.isPlayingSoundClick){
            soundManager.playSound()
        }
    }
}

data class HomeUiState(
    val bestScore: Int = 0,
    val isShowDialogChooseAvatar: Boolean = false,
    val avatarId: Int = R.drawable.img_1,
    val isPlayingSoundClick: Boolean = true,
    val isShowDialogSelectLevel: Boolean = false,
)