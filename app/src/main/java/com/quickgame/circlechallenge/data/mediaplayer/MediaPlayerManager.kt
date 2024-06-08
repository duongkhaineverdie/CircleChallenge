package com.quickgame.circlechallenge.data.mediaplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.domain.interactors.GetStatusPlaySoundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MediaPlayerManager(
    context: Context,
    private val getStatusPlaySoundUseCase: GetStatusPlaySoundUseCase,
) {
    private val mediaPlayer = MediaPlayer.create(context, R.raw.game_background)

    init {
        playMusicBackground()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            getStatusPlaySoundUseCase(Unit).collect {result->
                result.onSuccess {
                    if (it) {
                        mediaPlayer.start()
                    } else {
                        mediaPlayer.pause()
                    }
                }
            }
        }
    }

    private fun playMusicBackground() {
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            isLooping = true
            start()
        }
    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun resumeMusic() {
        mediaPlayer.start()
    }

}