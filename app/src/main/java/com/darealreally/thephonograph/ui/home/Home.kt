package com.darealreally.thephonograph.ui.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darealreally.thephonograph.R
import com.darealreally.thephonograph.data.Song
import com.darealreally.thephonograph.data.TestData
import com.darealreally.thephonograph.ui.cd.CDCase
import com.darealreally.thephonograph.ui.cd.TopCDCase
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme
import com.darealreally.thephonograph.utils.toTimeFormat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.systemBarsPadding

// Stateful
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(context = LocalContext.current)
    ),
    toPlayerScreen: () -> Unit,
    backToHome: () -> Unit
) {
    // Props
    val viewState by viewModel.state.collectAsState()

    // Side Effect
    LaunchedEffect(viewState.selectedSong) {
        viewState.selectedSong?.let { _ ->
            toPlayerScreen()
            return@LaunchedEffect
        }
        backToHome()
    }

    // UI
    HomeContent(
        songs = viewState.songs,
        top10Songs = viewState.top10Songs,
        latestReleasedSongs = viewState.latestReleasedSongs,
        songPlaying = viewState.songPlaying,
        onSelectSong = { viewModel.onSelectSong(it) }
    )
}

// Stateless
@Composable
fun HomeContent(
    songs: List<Song>,
    top10Songs: List<Song>,
    latestReleasedSongs: List<Song>,
    songPlaying: Song?,
    onSelectSong: (Song?) -> Unit
) {
    Log.d("HomeContent", "called")
    // Props
    val windowInsets = LocalWindowInsets.current
    val topInset = with(LocalDensity.current) { windowInsets.systemBars.top.toDp() }
    val topInsetDp = if (topInset == 0.dp) 0.dp else topInset - 10.dp

    val onClickSong: (Song) -> Unit = {
        onSelectSong(it)
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        // Layer 1: TOP BAR
        TopBar(
            modifier = Modifier
                .padding(top = topInsetDp)
                .zIndex(1F)
        )

        // Layer 2: CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = topInsetDp + 90.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            CategorySongs(
                category = "Top 10",
                songs = top10Songs,
                topSongs = true,
                onClickSong = onClickSong
            )

            CategorySongs(
                category = "Recently Played",
                songs = songs,
                onClickSong = onClickSong
            )

            CategorySongs(
                category = "Latest Release",
                songs = latestReleasedSongs,
                onClickSong = onClickSong
            )

            Spacer(
                modifier = Modifier.padding(bottom = 30.dp)
            )

        } //: Column

    } //: Box
}

@Composable
fun CategorySongs(
    category: String,
    songs: List<Song>,
    topSongs: Boolean = false,
    onClickSong: (Song) -> Unit = {}
) {
    // Props
    val context = LocalContext.current

    // UI
    Column {
        // Row 1: RECENTLY PLAYED
        Text(
            text = category,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.9F),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 21.dp)
        )

        LazyRow {

            items(
                items = songs,
                key = { it.id }
            ) {

                val imageId = context.resources.getIdentifier(
                    it.albumArtName.lowercase(),
                    "drawable",
                    context.packageName
                )

                if (topSongs) {
                    TopCDCase(
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .padding(start = if (it.id == "1") 12.dp else 0.dp),
                        rank = it.rankNumber.toString(),
                        lastPos = it.lastPosDiff.toString(),
                        imageId = imageId,
                        onClick = { onClickSong(it) }
                    )
                } else {
                    CDCase(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .padding(start = if (it.id == "1") 21.dp else 0.dp),
                        imageId = imageId,
                        time = it.duration.toTimeFormat(),
                        onClick = { onClickSong(it) }
                    )
                } //: if

            } //: items

        } //: LazyRow

    } //: Column
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    // Props
    val onBackground = MaterialTheme.colors.background

    // UI
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        onBackground,
                        onBackground.copy(alpha = 0.1F)
                    )
                )
            )
            .padding(horizontal = 21.dp)
            .padding(bottom = 5.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        // Row 1:
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Col 1: APP NAME
            Text(
                text = "The Phonograph",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.weight(1F)
            )

            // Col 2: PROFILE
            Image(
                painter = painterResource(R.drawable.ic_profile_image_placeholder),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colors.onBackground.copy(alpha = 0.4F)
                ),
                modifier = Modifier
                    .size(37.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colors.onBackground.copy(0.2F)
                    )
                    .clickable(onClick = {})
                    .offset(y = 6.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Row 2:
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            // Col 1:
            Text(
                text = "Played 201",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5F),
            )

            // Col 2:
            Text(
                text = "Model 1900",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5F),
            )

            Spacer(modifier = Modifier.weight(1F))

        }

    }
}


/**
 * Preview Section
 */
@Preview
@Composable
fun HomeContentPreview() {
    ThePhonographTheme {
        HomeContent(
            songs = TestData.songs,
            top10Songs = TestData.songs,
            latestReleasedSongs = TestData.songs,
            songPlaying = TestData.song,
            onSelectSong = {}
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    ThePhonographTheme {
        TopBar()
    }
}