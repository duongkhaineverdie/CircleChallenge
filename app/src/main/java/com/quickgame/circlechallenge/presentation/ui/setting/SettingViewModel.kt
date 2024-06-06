package com.quickgame.circlechallenge.presentation.ui.setting

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.quickgame.circlechallenge.BuildConfig
import com.quickgame.circlechallenge.data.soundmanager.SoundManager
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import com.quickgame.circlechallenge.domain.interactors.SetStatusPlaySoundUseCaseMatchGame
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class SettingViewModel(
    savedStateHandle: SavedStateHandle,
    private val getStatusPlaySoundUseCase: GetStatusPlaySoundUseCase,
    private val setStatusPlaySoundUseCaseMatchGame: SetStatusPlaySoundUseCaseMatchGame,
    private val soundManager: SoundManager,
    private val context: Application
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<SettingUiState> = MutableStateFlow(SettingUiState())
    val stateFlow: StateFlow<SettingUiState> = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            getStatusPlaySoundUseCase(Unit).collect { result ->
                result.onSuccess {
                    _stateFlow.update {state ->
                        state.copy(
                            isPlayingSoundClick = it
                        )
                    }
                }
            }
        }
        getSizeCache()
        val versionCode = BuildConfig.VERSION_CODE
        _stateFlow.value = SettingUiState(
            versionCode = versionCode.toString()
        )
    }
    private fun getSizeCache(){
        try {
            val sizeCache = getCacheSize()
            _stateFlow.value = SettingUiState(
                sizeCache = (sizeCache / (1024 * 1024)).toString(),
            )
        }catch (_: Exception){

        }
    }

    private fun getCacheSize(): Long {
        var totalSize = 0L
        totalSize += getDirSize(context.cacheDir)
        if (context.externalCacheDir != null) {
            totalSize += getDirSize(context.externalCacheDir!!)
        }
        return totalSize
    }

    private fun getDirSize(dir: File): Long {
        var size = 0L
        if (dir.isDirectory) {
            for (file in dir.listFiles()!!) {
                size += if (file.isFile) file.length() else getDirSize(file)
            }
        }
        return size
    }

    fun clearCache() {
        setIsPlayingSoundAfterClearCache()
        if (context.cacheDir.deleteRecursively()){
            getSizeCache()
        }
    }

    fun setStatusPlaySound() {
        viewModelScope.launch {
            setStatusPlaySoundUseCaseMatchGame(!stateFlow.value.isPlayingSoundClick)
        }
    }

    private fun setIsPlayingSoundAfterClearCache(){
        viewModelScope.launch {
            setStatusPlaySoundUseCaseMatchGame(true)
        }
    }

    fun playSoundClick() {
        if (stateFlow.value.isPlayingSoundClick){
            soundManager.playSound()
        }
    }

}

data class SettingUiState(
    val versionCode: String = "",
    val sizeCache: String = "",
    val isPlayingSoundClick: Boolean = true,
)