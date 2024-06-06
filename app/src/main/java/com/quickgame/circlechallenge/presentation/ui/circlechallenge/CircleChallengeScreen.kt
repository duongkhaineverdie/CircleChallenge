package com.quickgame.circlechallenge.presentation.ui.circlechallenge

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.domain.navigation.Destination
import com.quickgame.circlechallenge.presentation.ui.circlechallenge.components.GamePlayComponent
import com.quickgame.circlechallenge.presentation.ui.component.CustomDialog
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import com.quickgame.circlechallenge.utils.TAG
import org.koin.androidx.compose.koinViewModel

@Composable
fun CircleChallengeScreen(navController: NavHostController) {
    // TODO UI Rendering
    val circleChallengeViewModel: CircleChallengeViewModel = koinViewModel()
    val uiState by circleChallengeViewModel.stateFlow.collectAsState()
    CircleChallengeScreen(
        modifier = Modifier.fillMaxSize(),
        isDefeated = uiState.isDefeat,
        onDefeat = {
            circleChallengeViewModel.playSoundLose()
            circleChallengeViewModel.onDefeat(score = it)
        },
        bestScore = uiState.bestScore,
        score = uiState.score,
        onBack = {
            circleChallengeViewModel.playSoundClick()
            navController.popBackStack()
        },

        onRestart = {
            circleChallengeViewModel.playSoundClick()
            circleChallengeViewModel.onRestart()
        },
        onSetting = {
            circleChallengeViewModel.playSoundClick()
            navController.navigate(Destination.SettingScreen.fullRoute)
        },
        onClickGame = {
            circleChallengeViewModel.playSoundClick()
        }
    )
}

@Composable
fun CircleChallengeScreen(
    modifier: Modifier = Modifier,
    isDefeated: Boolean = true,
    bestScore: Int = 0,
    score: Int = 0,
    onDefeat: (Int) -> Unit = {/* no-op */ },
    onBack: () -> Unit = {/* no-op */ },
    onRestart: () -> Unit = {/* no-op */ },
    onSetting: () -> Unit = {/* no-op */ },
    onClickGame: () -> Unit = {/* no-op */ },
) {
    Box(
        modifier = modifier
            .background(Color(0xFF1E2436))
    ) {

        AnimatedContent(
            isDefeated,
            label = "basic_transition",
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) { targetState ->
            if (targetState) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = score.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 150.sp,
                    )
                    Log.d(TAG, "CircleChallengeScreen: $score - $bestScore")
                    if (score > bestScore) {
                        Text(
                            text = stringResource(id = R.string.label_new_best),
                            fontWeight = FontWeight.Bold,
                            fontSize = 50.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(60.dp)
                                .aspectRatio(1f),
                            contentPadding = PaddingValues(5.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Button(
                            onClick = onRestart,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(100.dp)
                                .aspectRatio(1f),
                            contentPadding = PaddingValues(5.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Button(
                            onClick = onSetting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(60.dp)
                                .aspectRatio(1f),
                            contentPadding = PaddingValues(5.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            } else {
                GamePlayComponent(
                    onDefeat = onDefeat,
                    onClickGame = onClickGame
                )
            }
        }
    }

}

@Composable
@Preview(name = "CircleChallenge")
private fun CircleChallengeScreenPreview() {
    CircleChallengeTheme {
        CircleChallengeScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
