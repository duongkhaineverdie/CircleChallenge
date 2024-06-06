package com.quickgame.circlechallenge.presentation.ui.setting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quickgame.circlechallenge.R
import com.quickgame.circlechallenge.presentation.ui.component.GameDefaultButton
import com.quickgame.circlechallenge.presentation.ui.theme.BackgroundBoardGameColor
import com.quickgame.circlechallenge.presentation.ui.theme.CircleChallengeTheme

@Composable
fun RowGameCardSetting(
    modifier: Modifier = Modifier,
    titleFunction: String,
    detailFunction: String,
    iconId: Int = R.drawable.ic_speaker,
    checked: Boolean = true,
    isShowIcon: Boolean = true,
    isShowSwitch: Boolean = true,
    onClickItem: () -> Unit = {/* no-op */ },
    clickable: Boolean = true,
) {
    Box(
        modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = if (clickable) rememberRipple(
                bounded = true,
                radius = 250.dp,
                color = Color.DarkGray
            ) else null,
            onClick = { onClickItem() },
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GameDefaultButton(
                color = BackgroundBoardGameColor,
                modifier = Modifier
                    .size(50.dp)
                    .alpha(if (isShowIcon) 1f else 0f),
                paddingEffect = 5.dp,
                enabled = false
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = titleFunction, color = Color.Black)
                Text(
                    text = detailFunction,
                    color = Color.Gray
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = null,
                modifier = Modifier
                    .alpha(if (isShowSwitch) 1f else 0f),
                )
        }
    }
}

@Preview(name = "RowGameCardSettingComponent")
@Composable
private fun PreviewRowGameCardSettingComponent() {
    CircleChallengeTheme {
        RowGameCardSetting(titleFunction = "", detailFunction = "")
    }
}