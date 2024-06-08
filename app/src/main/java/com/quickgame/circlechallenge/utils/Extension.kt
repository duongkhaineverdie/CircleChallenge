package com.quickgame.circlechallenge.utils

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
)

fun Modifier.bounceClick(
    enabled: Boolean = true,
    onClick: () -> Unit = {/* no-op */},
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    //Animate scale
    /*val scale by animateFloatAsState(if (isPressed) 0.9f else 1f,
        label = ""
    )*/

    val scale = if (isPressed) 0.9f else 1f
    if (enabled){
        this
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    }else{
        this
    }
}


fun DrawScope.drawDetailedBall(position: Offset, radius: Float) {
    drawCircle(
        color = Color.White,
        radius = radius,
        center = position
    )

    val blackHexagonRadius = radius / 3
    val whiteHexagonRadius = radius / 3.5f

    // Draw central hexagon
    drawHexagon(position, blackHexagonRadius, Color.Black)

    // Draw surrounding hexagons
    val angle = 60f
    for (i in 0..5) {
        val rad = Math.toRadians((angle * i).toDouble())
        val hexagonCenter = Offset(
            x = position.x + (cos(rad) * radius).toFloat() / 1.5f,
            y = position.y + (sin(rad) * radius).toFloat() / 1.5f
        )
        drawHexagon(hexagonCenter, whiteHexagonRadius, Color.Black)
    }
}

fun DrawScope.drawHexagon(center: Offset, radius: Float, color: Color) {
    val path = androidx.compose.ui.graphics.Path()
    val angle = 60f
    for (i in 0 until 6) {
        val rad = Math.toRadians((angle * i).toDouble())
        val x = center.x + (cos(rad) * radius).toFloat()
        val y = center.y + (sin(rad) * radius).toFloat()
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    drawPath(path, color)
}