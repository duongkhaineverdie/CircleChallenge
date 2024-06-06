package com.quickgame.circlechallenge.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.presentation.ui.component.AvatarPlayerComponent
import com.quickgame.circlechallenge.presentation.ui.component.CustomDialog
import com.quickgame.circlechallenge.presentation.ui.component.GameDefaultButton
import com.quickgame.circlechallenge.presentation.ui.theme.BackgroundBoardGameColor
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme
import com.quickgame.circlechallenge.utils.Constants

@Composable
fun AvatarSelectionDialog(
    isShowDialog: Boolean = true,
    onDismissRequest: () -> Unit = {/* no-op */ },
    onClickChooseAvatar: (Int) -> Unit = {/* no-op */ },
    avatarIdFirst: Int = R.drawable.img_1,
) {
    var resIdAvatarChoose by remember {
        mutableIntStateOf(avatarIdFirst)
    }
    CustomDialog(isShowDialog = isShowDialog, onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.img_background_dialog),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(Constants.listImageAvatars()) {
                            AvatarPlayerComponent(
                                resId = it,
                                modifier = Modifier.aspectRatio(1f),
                                isShowFrame = resIdAvatarChoose == it,
                                onClick = {
                                    resIdAvatarChoose = it
                                }
                            )
                        }
                    }
                }
                GameDefaultButton(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 40.dp)
                        .align(Alignment.BottomCenter),
                    color = BackgroundBoardGameColor,
                    paddingEffect = 5.dp,
                    onClick = { onClickChooseAvatar(resIdAvatarChoose) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = stringResource(id = R.string.label_select),
                        fontSize = 24.sp,
                    )
                }
            }
        }
    }
}

@Preview(name = "AvatarSelectionDialog")
@Composable
private fun PreviewAvatarSelectionDialog() {
    CircleChallengeTheme {
        AvatarSelectionDialog(
            isShowDialog = true,
        )
    }
}