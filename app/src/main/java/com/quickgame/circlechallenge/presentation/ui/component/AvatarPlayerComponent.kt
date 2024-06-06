package com.quickgame.circlechallenge.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quickgame.circlechallenge.R

@Composable
fun AvatarPlayerComponent(
    modifier: Modifier = Modifier,
    resId: Int = R.drawable.img_1,
    onClick: () -> Unit = {/* no-op */ },
    isShowFrame: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val matrix = ColorMatrix()
    val darkFactor =
        0.7f // Adjust this value between 0.0f (very dark) and 1.0f (original brightness)

// Create a new ColorMatrix with adjusted luminosity
    val newMatrix = ColorMatrix(
        floatArrayOf(
            darkFactor, 0f, 0f, 0f, 0f,
            0f, darkFactor, 0f, 0f, 0f,
            0f, 0f, darkFactor, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    matrix.set(newMatrix)

    Box(modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.85f)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.8f),
                contentScale = ContentScale.Crop,
                colorFilter = if (isPressed) ColorFilter.colorMatrix(matrix) else null
            )
        }
        if (isShowFrame) {
            Image(
                painter = painterResource(id = R.drawable.img_frame),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "FrameAvatarComponent")
@Composable
private fun PreviewFrameAvatarComponent() {
    AvatarPlayerComponent(
        modifier = Modifier.size(100.dp)
    )
}