package com.darealreally.thephonograph.ui.cd

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.darealreally.thephonograph.R
import com.darealreally.thephonograph.data.TestData
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme

enum class PlayStatus {
    Play,
    Pause
}

@Composable
fun TopCDCase(
    modifier: Modifier = Modifier,
    scale: Float = 0.19F,
    imageId: Int? = null,
    rank: String,
    lastPos: String,
    onClick: () -> Unit = {}
) {
    // Props
    val roundPer = 5

    // Transition
    val xOffset = remember { Animatable(28F) }

    // Side Effect
    LaunchedEffect(Unit) {
        xOffset.animateTo(
            targetValue = 105F
        )
    }

    // UI
    Box(
        modifier = modifier.width(230.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        // Layer 1: Case
        Box(
            modifier = modifier
                .width(160.dp)
                .height(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.05F),
                    shape = RoundedCornerShape(roundPer)
                )
                .clip(RoundedCornerShape(roundPer))
                .clickable(
                    onClick = onClick
                )
                .padding(all = 20.dp)
        ) {

            CD(
                modifier = Modifier.alpha(0F),
                scale = scale,
                imageId = imageId
            )

            Text(
                modifier = Modifier.offset(y = (-22).dp),
                text = rank,
                style = MaterialTheme.typography.h5,
                fontSize = 90.sp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.1F)
            )

            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = lastPos,
                style = MaterialTheme.typography.h5,
                fontSize = 26.sp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.1F)
            )

        } //: Box

        // Layer 2: CD
        CD(
            modifier = Modifier
                .zIndex(-1F)
                .offset(x = xOffset.value.dp)
                .rotate(255F + xOffset.value),
            scale = scale,
            imageId = imageId
        )

    } //: Row


}

@Composable
fun CDCase(
    modifier: Modifier = Modifier,
    scale: Float = 0.2F,
    imageId: Int? = null,
    time: String,
    onPlayer: Boolean = false,
    onChangePlayStatus: (PlayStatus) -> Unit = {},
    onClick: () -> Unit = {}
) {
    // Props
    val roundPer = if (onPlayer) 8 else 5
    val fontSize = if (onPlayer) 16 else 12
    val extraPadding = if (onPlayer) 12 else 0

    // UI
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .background(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.05F),
                shape = RoundedCornerShape(roundPer)
            )
            .clip(RoundedCornerShape(roundPer))
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = (10 + extraPadding).dp)
            .padding(top = (10 + extraPadding).dp)
            .padding(bottom = (8 + extraPadding).dp)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            // Row 1: CD
            CD(
                scale = scale,
                imageId = imageId
            )

            // Row 2: PLAY or PAUSE BUTTON + TIME
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (!onPlayer) {
                    PlayOrPauseButton(onChangePlayStatus = onChangePlayStatus)
                }

                Spacer(
                    modifier = Modifier.weight(1F)
                )

                Text(
                    text = time,
                    style = MaterialTheme.typography.caption,
                    fontSize = fontSize.sp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.5F)
                )

            } //: Row

        } //: Column

    } //: Box
}

@Composable
fun PlayOrPauseButton(
    onPlayer: Boolean = false,
    onChangePlayStatus: (PlayStatus) -> Unit
) {
    // State
    var playStatus by remember { mutableStateOf(PlayStatus.Pause) }

    // Props
    val size = if (onPlayer) 34 else 8
    val extraPadding = if (onPlayer) 18 else 0
    val iconId =
        if (playStatus == PlayStatus.Play) R.drawable.ic_pause
        else R.drawable.ic_play
    val onBackground = MaterialTheme.colors.onBackground
    val iconXOffsetDp = if (onPlayer) 3.dp else 0.dp

    // UI
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25))
            .background(onBackground.copy(0.05F))
            .clickable(
                onClick = {
                    playStatus = when (playStatus) {
                        PlayStatus.Play -> {
                            PlayStatus.Pause
                        }
                        PlayStatus.Pause -> {
                            PlayStatus.Play
                        }
                    }
                    onChangePlayStatus(playStatus)
                }
            )
            .padding((8 + extraPadding).dp)
    ) {
        Image(
            modifier = Modifier
                .size(size.dp)
                .offset(x = iconXOffsetDp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = onBackground.copy(0.5F))
        )
    }
}





/**
 * Preview Section
 */
@Preview(
    name = "w/o Image",
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun CDCasePreview() {
    ThePhonographTheme {
        CDCase(
            modifier = Modifier.padding(10.dp),
            time = "1:00"
        )
    }
}

@Preview(
    name = "w/ Image",
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun CDCase2Preview() {
    ThePhonographTheme {
        CDCase(
            modifier = Modifier.padding(10.dp),
            imageId = R.drawable.ineverdie,
            time = "1:00"
        )
    }
}

@Preview(
    name = "w/ Image",
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun CDCase3Preview() {
    ThePhonographTheme {
        CDCase(
            modifier = Modifier.padding(10.dp),
            imageId = R.drawable.ineverdie,
            time = "1:00",
            onPlayer = true
        )
    }
}

@Preview(
    name = "Top CD Case",
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
fun TopCDCasePreview() {
    ThePhonographTheme {
        TopCDCase(
            modifier = Modifier.padding(10.dp),
            imageId = R.drawable.ineverdie,
            rank = TestData.song.rankNumber.toString(),
            lastPos = TestData.song.lastPosDiff.toString()
        )
    }
}

@Preview(
    name = "Play or Pause Button",
    backgroundColor = 0xFF000000,
    showBackground = true
)@Composable
fun PlayOrPausePreview() {
    ThePhonographTheme {
        PlayOrPauseButton(
            onChangePlayStatus = {}
        )
    }
}

@Preview(
    name = "Play or Pause Button",
    backgroundColor = 0xFF000000,
    showBackground = true
)@Composable
fun PlayOrPause2Preview() {
    ThePhonographTheme {
        PlayOrPauseButton(
            onChangePlayStatus = {},
            onPlayer = true
        )
    }
}