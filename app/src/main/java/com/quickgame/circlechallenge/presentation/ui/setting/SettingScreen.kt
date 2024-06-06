package com.quickgame.circlechallenge.presentation.ui.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.presentation.ui.component.GameDefaultButton
import com.quickgame.circlechallenge.presentation.ui.setting.components.CardSetting
import com.quickgame.circlechallenge.presentation.ui.setting.components.RowGameCardSetting
import com.quickgame.circlechallenge.presentation.ui.theme.BackgroundBoardGameColor
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import com.quickgame.circlechallenge.presentation.ui.theme.Purple80
import com.quickgame.circlechallenge.utils.TAG
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(navController: NavHostController) {
    // TODO UI Rendering
    val settingViewModel: SettingViewModel = koinViewModel()
    val uiState by settingViewModel.stateFlow.collectAsState()

    SettingScreen(
        modifier = Modifier.fillMaxSize(),
        onBackClick = {
            settingViewModel.playSoundClick()
            navController.navigateUp()
        },
        onClickChangeSound = {
            settingViewModel.playSoundClick()
            settingViewModel.setStatusPlaySound()
        },
        checkedSound = uiState.isPlayingSoundClick,
    )
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {/* no-op */ },
    onClickChangeSound: () -> Unit = {/* no-op */ },
    checkedSound: Boolean,
    versionCode: String = "1.0.0",
) {
    ConstraintLayout(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (header, body) = createRefs()
        Box(modifier = Modifier
            .constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
            .background(Purple80)
            .padding(horizontal = 10.dp)
            .statusBarsPadding()
            .height(60.dp)
        ) {
            GameDefaultButton(
                color = BackgroundBoardGameColor,
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart),
                paddingEffect = 5.dp,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.constrainAs(body) {
                top.linkTo(header.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardSetting(
                isShowUnderDivider = true,
                titleCard = stringResource(id = R.string.label_game)
            ) {
                RowGameCardSetting(
                    titleFunction = stringResource(id = R.string.label_sound),
                    iconId = R.drawable.ic_speaker,
                    detailFunction = stringResource(id = if (checkedSound) R.string.label_on else R.string.label_off),
                    checked = checkedSound,
                    onClickItem = {
                        Log.d(TAG, "SettingScreenChuan: $checkedSound")
                        onClickChangeSound()
                    }
                )
            }
            CardSetting(
                isShowUnderDivider = false,
                titleCard = stringResource(id = R.string.label_more)
            ) {
                RowGameCardSetting(
                    titleFunction = stringResource(id = R.string.version_app),
                    isShowIcon = false,
                    isShowSwitch = false,
                    detailFunction = versionCode,
                    clickable = false
                )
            }
        }
    }
}

@Composable
@Preview(name = "Setting", showSystemUi = true)
private fun SettingScreenPreview() {
    CircleChallengeTheme {
        SettingScreen(
            modifier = Modifier.fillMaxSize(),
            checkedSound = true
        )
    }
}
