package com.quickgame.circlechallenge.presentation.ui.circlechallenge.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun TouchPad(modifier: Modifier, onDrag: (Float) -> Unit) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidth = with(density) {
        configuration.screenWidthDp.dp.toPx()
    }
    val radiusTouchPad = with(density) { 30.dp.toPx() }
    val radiusTouchPadOutline = with(density) { 60.dp.toPx() }
    var touchPadX by remember { mutableFloatStateOf(screenWidth / 2) }
    Box(modifier = modifier
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    touchPadX = screenWidth / 2
                },
                onHorizontalDrag = { change, dragAmount ->
                    onDrag(dragAmount)
                    touchPadX = (touchPadX + dragAmount).coerceIn(
                        (screenWidth / 2) - radiusTouchPadOutline + radiusTouchPad,
                        screenWidth / 2 + radiusTouchPadOutline - radiusTouchPad
                    )
                    change.consume()
                }
            )
        }
    )
    Canvas(modifier = modifier) {
        val height = size.height
        val width = size.width
        val centerX = width / 2
        val centerY = height / 2

        drawCircle(
            color = Color.White,
            radius = radiusTouchPadOutline,
            center = Offset(centerX, centerY),
            style = Stroke(
                width = 5.dp.toPx()
            )
        )

        drawCircle(
            color = Color.White,
            radius = radiusTouchPad,
            center = Offset(touchPadX, centerY),
        )

    }
}