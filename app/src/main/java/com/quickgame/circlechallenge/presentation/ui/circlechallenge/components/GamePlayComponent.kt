package com.quickgame.circlechallenge.presentation.ui.circlechallenge.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GamePlayComponent(
    modifier: Modifier = Modifier,
    paddingAreaPlayerMove: Dp = 50.dp,
    playerSpeed: Float = 15f,
    playerSize: Float = 100f,
    obstaclesRadius: Float = 100f,
    playerColor: Color = Color(0xFF4AF4AA),
    onDefeat: (Int) -> Unit = {/* no-op */ },
    onClickGame: () -> Unit = {/* no-op */ },
    avatarId: Int = R.drawable.img_1
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = with(density) {
        configuration.screenHeightDp.dp.toPx()
    }
    val screenWidth = with(density) {
        configuration.screenWidthDp.dp.toPx()
    }

    // State to hold the falling objects
    var fallingObjects by remember { mutableStateOf(listOf<FallingObject>()) }
    val playerX = remember { mutableFloatStateOf(screenWidth / 2) }
    val playerY = (screenHeight) / 2 - playerSize / 2
    var playerDirection by remember { mutableStateOf(true) }

    var isPlayerBroken by remember { mutableStateOf(false) }

    // Player pieces animation
    var playerPieces by remember { mutableStateOf(emptyList<PlayerPiece>()) }

    var timePlayed by remember { mutableIntStateOf(0) }

    val imageBitmap = ImageBitmap.imageResource(id = avatarId)


    LaunchedEffect(key1 = isPlayerBroken) {
        if (isPlayerBroken) {
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
                    size = obstaclesRadius,
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
            val height = size.height
            val heightFirstLine = height / 18
            repeat(18) {
                val offset = heightFirstLine * it
                val colorLine = if (it % 2 == 0)
                    Color(0xFF39B549)
                else
                    Color(0xFF289444)
                drawRect(
                    color = colorLine,
                    topLeft = Offset(0f, offset),
                    size = Size(screenWidth, heightFirstLine),
                )
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val pitchPadding = 50f
            val pitchWidth = canvasWidth - 2 * pitchPadding
            val pitchHeight = canvasHeight - 2 * pitchPadding
            val centerX = pitchPadding + pitchWidth / 2
            val centerY = pitchPadding + pitchHeight / 2
            val centerCircleRadius = 100f
            val goalAreaWidth = 200f
            val goalAreaHeight = 100f

            drawLine(
                color = Color.White,
                start = Offset(pitchPadding, pitchPadding),
                end = Offset(pitchPadding + pitchWidth, pitchPadding),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.White,
                start = Offset(pitchPadding, pitchPadding),
                end = Offset(pitchPadding, pitchPadding + pitchHeight),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.White,
                start = Offset(pitchPadding + pitchWidth, pitchPadding),
                end = Offset(pitchPadding + pitchWidth, pitchPadding + pitchHeight),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.White,
                start = Offset(pitchPadding, pitchPadding + pitchHeight),
                end = Offset(pitchPadding + pitchWidth, pitchPadding + pitchHeight),
                strokeWidth = 5f
            )

            // Draw the center circle
            drawCircle(
                color = Color.White,
                radius = centerCircleRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 5f)
            )
            drawCircle(
                color = Color.White,
                radius = 10f,
                center = Offset(centerX, centerY),
            )

            // Draw the halfway line
            drawLine(
                color = Color.White,
                start = Offset(pitchPadding, centerY),
                end = Offset(pitchPadding + pitchWidth, centerY),
                strokeWidth = 5f
            )

            // Draw goal areas
            drawRect(
                color = Color.White,
                topLeft = Offset(centerX - goalAreaWidth / 2, pitchPadding),
                size = Size(goalAreaWidth, goalAreaHeight),
                style = Stroke(width = 5f)
            )
            drawRect(
                color = Color.White,
                topLeft = Offset(
                    centerX - goalAreaWidth / 2,
                    pitchPadding + pitchHeight - goalAreaHeight
                ),
                size = Size(goalAreaWidth, goalAreaHeight),
                style = Stroke(width = 5f)
            )

            // Draw the penalty areas
            val penaltyAreaWidth = 500f
            val penaltyAreaHeight = 200f
            drawRect(
                color = Color.White,
                topLeft = Offset(centerX - penaltyAreaWidth / 2, pitchPadding),
                size = Size(penaltyAreaWidth, penaltyAreaHeight),
                style = Stroke(width = 5f)
            )
            drawRect(
                color = Color.White,
                topLeft = Offset(
                    centerX - penaltyAreaWidth / 2,
                    pitchPadding + pitchHeight - penaltyAreaHeight
                ),
                size = Size(penaltyAreaWidth, penaltyAreaHeight),
                style = Stroke(width = 5f)
            )

            // Draw goal
            drawRect(
                color = Color.White,
                topLeft = Offset(centerX - goalAreaWidth / 4, pitchPadding - goalAreaHeight / 3),
                size = Size(goalAreaWidth / 2, goalAreaHeight / 3),
                style = Stroke(width = 5f)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(
                    centerX - goalAreaWidth / 4,
                    pitchPadding + pitchHeight
                ),
                size = Size(goalAreaWidth / 2, goalAreaHeight / 3),
                style = Stroke(width = 5f)
            )


            // Draw falling objects
            fallingObjects.forEach { obj ->
                val fraction = obj.yAnimatable.value / (screenHeight - obj.size)
                val currentX = obj.startX + (obj.endX - obj.startX) * fraction
                rotate(
                    obj.rotationAngle * (obj.yAnimatable.value / screenHeight),
                    pivot = Offset(currentX, obj.yAnimatable.value)
                ) {// Draw the ball
                    drawDetailedBall(Offset(currentX, obj.yAnimatable.value), obstaclesRadius)
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
                drawImage(
                    image = imageBitmap,
                    dstSize = IntSize(
                        playerSize.toInt(),
                        playerSize.toInt(),
                    ),
                    filterQuality = FilterQuality.Medium,
                    dstOffset = IntOffset((playerX.floatValue - playerSize / 2).toInt(),
                        playerY.toInt()
                    ),
                )
            }
        }
        AnimatedContent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
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