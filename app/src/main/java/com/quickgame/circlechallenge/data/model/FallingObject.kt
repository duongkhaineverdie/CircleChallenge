package com.quickgame.circlechallenge.data.model

import androidx.compose.animation.core.AnimationVector1D

data class FallingObject(
    val startX: Float,
    val endX: Float,
    val size: Float,
    val speed: Float,
    val rotationAngle: Float, // Angle of rotation
    val yAnimatable: androidx.compose.animation.core.Animatable<Float, AnimationVector1D>
)