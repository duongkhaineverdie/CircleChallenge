package com.quickgame.circlechallenge.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import com.quickgame.circlechallenge.utils.bounceClick
import com.quickgame.circlechallenge.utils.shadow

@Composable
fun GameDefaultButton(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit = {/* no-op */},
    paddingEffect: Dp = 20.dp,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick(
                enabled = enabled
            ) {
                onClick()
            }
            .clip(shape)
            .background(color)
            .padding(paddingEffect)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    color = Color.White.copy(0.3f),
                    offsetX = (-4).dp,
                    offsetY = (-4).dp,
                    blurRadius = 8.dp,
                )
                .shadow(
                    color = Color.Black.copy(0.3f),
                    offsetX = (4).dp,
                    offsetY = (4).dp,
                    blurRadius = 8.dp,
                )
                .clip(shape)
                .background(color)
        ) {
            content()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameDefaultButtonPreview() {
    CircleChallengeTheme {
        GameDefaultButton(
            modifier = Modifier
                .height(200.dp)
                .width(100.dp),
            color = Color.White,
            onClick = {/* no-op */ },
            shape = CircleShape
        ) {

        }
    }
}