package com.darealreally.thephonograph

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.darealreally.thephonograph.ui.home.Home
import com.darealreally.thephonograph.ui.player.Player
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ThePhonographApp() {
    // States
    val scope = rememberCoroutineScope()
    val offsetY = remember { androidx.compose.animation.core.Animatable(0F) }

    // Props
    val windowInsets = LocalWindowInsets.current
    val heightPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val topInsetPx = windowInsets.systemBars.top
    val bottomInsetPx = windowInsets.systemBars.bottom
    val totalHeightPx = remember(topInsetPx, bottomInsetPx) {
        heightPx + windowInsets.systemBars.top + windowInsets.systemBars.bottom
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Layer 1:
        Home(
            setOffsetY = { newYOffset ->
                Log.d("Root", "offsetY: ${offsetY.value}")
                scope.launch {
                    offsetY.animateTo(
                        targetValue = newYOffset
                    )
                }
             },
            offsetY = offsetY.value,
            totalHeightPx = totalHeightPx,
            onDragStopped = {
                // snap to max or min (hidden or visible)
                val threshold = totalHeightPx / 1.5
                scope.launch {
                    offsetY.animateTo(
                        targetValue =  if (offsetY.value < threshold) 0F
                        else totalHeightPx
                    )
                }
            }
        )

        // Layer 2:
        // 0 px - visible
        // height px - hidden (default)
        Player(
            modifier = Modifier
                .zIndex(2F)
                .offset {
                    IntOffset(
                        x = 0,
                        y = offsetY.value.roundToInt()
                    )
                },
            onDismiss = {
                scope.launch {
                    offsetY.animateTo(
                        targetValue = totalHeightPx
                    )
                }
            },
            isAtHome = offsetY.value >= totalHeightPx
        )

    } //: Box
}