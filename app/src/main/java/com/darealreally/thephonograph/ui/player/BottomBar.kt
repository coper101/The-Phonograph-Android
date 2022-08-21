package com.darealreally.thephonograph.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darealreally.thephonograph.data.Song
import com.darealreally.thephonograph.data.TestData
import com.darealreally.thephonograph.ui.cd.CD
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme
import com.darealreally.thephonograph.utils.toTimeFormat
import com.google.accompanist.insets.LocalWindowInsets

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    songPlaying: Song,
    songPlayingTime: Int
) {
    // Props
    val windowInsets = LocalWindowInsets.current
    val bottomInsetDp = with(LocalDensity.current) { windowInsets.systemBars.bottom.toDp() }

    val onBackground = MaterialTheme.colors.onBackground
    val background = MaterialTheme.colors.background

    val context = LocalContext.current
    val imageId = remember(songPlaying.albumArtName) {
        context.resources.getIdentifier(
            songPlaying.albumArtName.lowercase(),
            "drawable",
            context.packageName
        )
    }
    
    // UI
    Row(
        modifier = Modifier
            .background(
                color = background.copy(alpha = 0.9F)
            )
            .padding(bottom = bottomInsetDp)
            .fillMaxWidth()
            .height(62.dp)
            .then(modifier)
    ) {
        // Col 1: CD
        CD(
            modifier = Modifier
                .requiredSize(124.dp)
                .offset(y = 37.dp)
                .drawWithContent {
                    clipRect(bottom = size.height / 2.2F) {
                        (this@drawWithContent).drawContent()
                    }
                },
            scale = 0.15F,
            imageId = imageId,
            spin = true
        )

        // Col 2: DESC
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 4.dp,
                    start = 8.dp,
                    end = 18.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            // TITLE + SINGER
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                text = "${songPlaying.title} - ${songPlaying.singer}".uppercase(),
                style = MaterialTheme.typography.subtitle1,
                color = onBackground.copy(alpha = 0.9F),
                maxLines = 1
            )

            // TIME
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NOW PLAYING",
                    style = MaterialTheme.typography.subtitle1,
                    color = onBackground.copy(alpha = 0.5F)
                )
                Text(
                    text = songPlayingTime.toTimeFormat(),
                    style = MaterialTheme.typography.subtitle1,
                    color = onBackground.copy(alpha = 0.7F)
                )
            } //: Row

        } //: Column

    } //: Row
}

@Preview
@Composable
fun BottomBarPreview() {
    ThePhonographTheme {
        BottomBar(
            songPlaying = TestData.song,
            songPlayingTime = 0
        )
    }
}