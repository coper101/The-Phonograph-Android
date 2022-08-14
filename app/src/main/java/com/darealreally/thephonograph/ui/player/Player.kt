package com.darealreally.thephonograph.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darealreally.thephonograph.R
import com.darealreally.thephonograph.data.Song
import com.darealreally.thephonograph.data.TestData
import com.darealreally.thephonograph.ui.cd.CDCase
import com.darealreally.thephonograph.ui.cd.PlayOrPauseButton
import com.darealreally.thephonograph.ui.cd.PlayStatus
import com.darealreally.thephonograph.ui.home.HomeViewModel
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme
import com.darealreally.thephonograph.utils.toTimeFormat
import com.google.accompanist.insets.LocalWindowInsets

// Stateful
@Composable
fun Player(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(context = LocalContext.current)
    )
) {
    val viewState by viewModel.state.collectAsState()

    if (viewState.selectedSong != null) {
        PlayerContent(
            selectedSong = viewState.selectedSong!!,
            onSelectSong = { viewModel.onSelectSong(it) },
            isSongPlaying = viewState.isSongPlaying,
            onChangeSongPlaying = { viewModel.onChangeSongPlaying(it) }
        )
    }
}

@Composable
fun PlayerContent(
    selectedSong: Song,
    onSelectSong: (Song?) -> Unit,
    isSongPlaying: Boolean,
    onChangeSongPlaying: (Song?) -> Unit
) {
    // Props
    val context = LocalContext.current
    val onBackground = MaterialTheme.colors.onBackground

    val windowInsets = LocalWindowInsets.current
    val topInset = with(LocalDensity.current) { windowInsets.systemBars.top.toDp() }
    val topInsetDp = if (topInset == 0.dp) 0.dp else topInset - 10.dp

    val bottomInset = with(LocalDensity.current) { windowInsets.systemBars.bottom.toDp() }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        // Layer 1: TOP BAR
        TopBar(
            modifier = Modifier.padding(top = topInsetDp),
            songTitle = selectedSong.title,
            songSinger = selectedSong.singer,
            dismissPlayer = { onSelectSong(null) }
        )

        // Layer 2: CONTENT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topInsetDp + 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CD
            CDCase(
                time = selectedSong.duration.toTimeFormat(),
                onPlayer = true,
                scale = 0.35F,
                imageId = context.resources.getIdentifier(
                    selectedSong.albumArtName.lowercase(),
                    "drawable",
                    context.packageName
                )
            )

            Spacer(modifier = Modifier.weight(1F))

            // LYRICS
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = selectedSong.lyricsByLine[0],
                    style = MaterialTheme.typography.subtitle1,
                    color = onBackground.copy(alpha = 0.5F)
                )

                Text(
                    text = selectedSong.lyricsByLine[1],
                    style = MaterialTheme.typography.subtitle2,
                    color = onBackground.copy(alpha = 0.9F)
                )

                Text(
                    text = selectedSong.lyricsByLine[2],
                    style = MaterialTheme.typography.subtitle1,
                    color = onBackground.copy(alpha = 0.5F)
                )

            } //: Column

            Spacer(modifier = Modifier.weight(1F))

            // PLAY BUTTON
            PlayOrPauseButton(
                onChangePlayStatus = {
                     when (it) {
                         PlayStatus.Play -> onChangeSongPlaying(selectedSong)
                         PlayStatus.Pause -> onChangeSongPlaying(null)
                     }
                },
                onPlayer = true
            )

            Spacer(modifier = Modifier.weight(1F))

            // MODEL
            Text(
                modifier = Modifier.padding(bottom = bottomInset + 10.dp),
                text = "${if (isSongPlaying) "Playing" else "Play"} on Model 1900",
                style = MaterialTheme.typography.subtitle1,
                color = onBackground.copy(alpha = 0.5F)
            )

        } //: Column

    } //: Box
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    songTitle: String,
    songSinger: String,
    dismissPlayer: () -> Unit = {}
) {
    // Props
    val onBackground = MaterialTheme.colors.onBackground

    // UI
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(
                MaterialTheme.colors.background
            )
            .padding(horizontal = 21.dp)
            .padding(bottom = 14.dp),
        contentAlignment = Alignment.Center
    ) {

        // Layer 1: TITLE + DESC
        Column(
            modifier = Modifier.padding(end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "PLAYING",
                style = MaterialTheme.typography.subtitle1,
                color = onBackground.copy(alpha = 0.5F)
            )

            Text(
                text = "$songTitle - $songSinger".uppercase(),
                style = MaterialTheme.typography.subtitle1,
                color = onBackground.copy(alpha = 1F),
                maxLines = 2,
                textAlign = TextAlign.Center
            )

        }

        // Layer 2: MINIMIZE
        Image(
            painter = painterResource(id = R.drawable.ic_minimize),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(48.dp)
                .padding(12.dp)
                .clickable(
                    onClick = dismissPlayer
                ),
            colorFilter = ColorFilter.tint(color = onBackground.copy(alpha = 0.3F))
        )

    } //: Box
}

@Preview
@Composable
fun PlayerContentPreview() {
    ThePhonographTheme {
        PlayerContent(
            selectedSong = TestData.song,
            onSelectSong = {},
            isSongPlaying = false,
            onChangeSongPlaying = {}
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    ThePhonographTheme {
        TopBar(
            songTitle = TestData.song.title,
            songSinger = TestData.song.singer
        )
    }
}