package com.quickgame.circlechallenge.presentation.ui.circlechallenge.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GamePlayComponent(
    modifier: Modifier = Modifier,
    paddingAreaPlayerMove: Dp = 50.dp,
    playerSpeed: Float = 20f,
    playerSize: Float = 80f,
    obstaclesSize: Float = 50f,
    playerColor: Color = Color(0xFF4AF4AA),
    onDefeat: (Int) -> Unit = {/* no-op */ },
    onClickGame: () -> Unit = {/* no-op */}
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = with(density) {
        configuration.screenHeightDp.dp.toPx() - 399f
    }
    val screenWidth = with(density) {
        configuration.screenWidthDp.dp.toPx()
    }

    // State to hold the falling objects
    var fallingObjects by remember { mutableStateOf(listOf<FallingObject>()) }
    val playerX = remember { mutableFloatStateOf(screenWidth / 2) }
    val playerY = screenHeight / 2 - playerSize / 2
    var playerDirection by remember { mutableStateOf(true) }

    var isPlayerBroken by remember { mutableStateOf(false) }

    // Player pieces animation
    var playerPieces by remember { mutableStateOf(emptyList<PlayerPiece>()) }

    var timePlayed by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = isPlayerBroken) {
        if (isPlayerBroken){
            onDefeat(timePlayed)
        }
    }

    // Coroutine to manage the game loop and difficulty
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            var spawnInterval = 1000L
            while (true) {
                // Increase difficulty over time
                delay(spawnInterval)
                spawnInterval = maxOf(200L, spawnInterval - 10L)

                // Add a new falling object
                val newObject = FallingObject(
                    startX = Random.nextFloat() * screenWidth,
                    endX = Random.nextFloat() * screenWidth,
                    size = obstaclesSize,
                    speed = 5f + Random.nextFloat() * 5f,
                    rotationAngle = 360f - Random.nextFloat() * 720f,
                    yAnimatable = Animatable(0f)
                )
                fallingObjects = fallingObjects + newObject

                // Animate the new falling object
                scope.launch {
                    newObject.yAnimatable.animateTo(
                        targetValue = screenHeight - newObject.size,
                        animationSpec = tween(
                            durationMillis = (30000 / newObject.speed).toInt(),
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
        scope.launch {
            while (true) {
                delay(1000L)
                timePlayed++
            }
        }
    }

    // Detect collisions
    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                delay(16L)
                // Draw player
                val playerRect = Rect(
                    offset = Offset(playerX.floatValue - playerSize / 2, playerY),
                    size = Size(playerSize, playerSize),
                )

                val collision = fallingObjects.any { obj ->
                    val fraction = obj.yAnimatable.value / (screenHeight - obj.size)
                    val currentX = obj.startX + (obj.endX - obj.startX) * fraction

                    val objRect = Rect(
                        offset = Offset(currentX, obj.yAnimatable.value),
                        size = Size(obj.size, obj.size),
                    )
                    rectIntersects(playerRect, objRect)
                }
                if (collision) {
                    // Handle game over or collision
                    isPlayerBroken = true
                    fallingObjects = emptyList()
                    val numPieces = 20 // Number of pieces to break into
                    val pieceSize = playerSize / 3
                    playerPieces = (0 until numPieces).map {
                        val angle = Random.nextFloat() * 2 * PI.toFloat()
                        PlayerPiece(
                            x = playerX.floatValue + playerSize / 2,
                            y = playerY + playerSize / 2,
                            angle = angle,
                            size = pieceSize,
                            speedX = cos(angle) * (Random.nextFloat() / 2 + 0.5f) * 100,
                            speedY = sin(angle) * (Random.nextFloat() / 2 + 0.5f) * 100
                        )
                    }
                }
            }
        }
    }

    // Update player pieces position
    LaunchedEffect(playerPieces) {
        while (isPlayerBroken && playerPieces.isNotEmpty()) {
            delay(16L)
            playerPieces = playerPieces.map { piece ->
                piece.copy(
                    x = piece.x + piece.speedX,
                    y = piece.y + piece.speedY,
                    speedY = piece.speedY // Gravity effect
                )
            }.filter { it.y < screenHeight }
        }
    }

    // Update player position
    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            if (playerDirection) {
                playerX.floatValue += playerSpeed
                if (playerX.floatValue >= screenWidth - playerSize - with(density) { paddingAreaPlayerMove.toPx() }) {
                    playerDirection = false
                }
            } else {
                playerX.floatValue -= playerSpeed
                if (playerX.floatValue <= 0 + with(density) { paddingAreaPlayerMove.toPx() } + playerSize) {
                    playerDirection = true
                }
            }
        }
    }

    Box(
        modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    // Change player direction immediately on tap
                    onClickGame()
                    playerDirection = !playerDirection
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFF101827),
                topLeft = Offset(0f + paddingAreaPlayerMove.toPx(), playerY),
                size = Size(screenWidth - paddingAreaPlayerMove.toPx() * 2, playerSize),
                cornerRadius = CornerRadius(screenWidth)
            )

            // Draw falling objects
            fallingObjects.forEach { obj ->
                val fraction = obj.yAnimatable.value / (screenHeight - obj.size)
                val currentX = obj.startX + (obj.endX - obj.startX) * fraction
                rotate(
                    obj.rotationAngle * (obj.yAnimatable.value / screenHeight),
                    pivot = Offset(currentX, obj.yAnimatable.value)
                ) {
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(currentX, obj.yAnimatable.value),
                        size = Size(obj.size, obj.size),
                    )
                }
            }

            // Draw player pieces
            playerPieces.forEach { piece ->
                drawRoundRect(
                    color = playerColor,
                    topLeft = Offset(piece.x, piece.y),
                    size = Size(piece.size, piece.size),
                    cornerRadius = CornerRadius(20f)
                )
            }
            if (!isPlayerBroken) {
                drawRoundRect(
                    color = playerColor,
                    topLeft = Offset(playerX.floatValue - playerSize / 2, playerY),
                    size = Size(playerSize, playerSize),
                    cornerRadius = CornerRadius(screenWidth)
                )
            }
        }
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            targetState = timePlayed,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    (scaleIn()).togetherWith(
                        scaleOut()
                    )
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    (scaleIn()).togetherWith(
                        scaleOut()
                    )
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }, label = ""
        ) { targetCount ->
            Text(
                text = targetCount.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 150.sp,
            )
        }
    }
}

// Function to check if two rectangles intersect
fun rectIntersects(rect1: Rect, rect2: Rect): Boolean {
    return (rect1.left < rect2.right && rect1.right > rect2.left
            && rect1.top < rect2.bottom && rect1.bottom > rect2.top)
}


data class FallingObject(
    val startX: Float,
    val endX: Float,
    val size: Float,
    val speed: Float,
    val rotationAngle: Float, // Angle of rotation
    val yAnimatable: Animatable<Float, AnimationVector1D>
)

data class PlayerPiece(
    val x: Float,
    val y: Float,
    val angle: Float,
    val size: Float,
    val speedX: Float,
    val speedY: Float
)

@Preview(name = "GamePlayComponent", showSystemUi = true)
@Composable
private fun PreviewGamePlayComponent() {
    CircleChallengeTheme {
        GamePlayComponent(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E2436))
        )
    }
}