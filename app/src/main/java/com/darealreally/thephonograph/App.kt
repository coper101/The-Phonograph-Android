package com.darealreally.thephonograph

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.darealreally.thephonograph.ui.home.Home
import com.darealreally.thephonograph.ui.player.Player
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme
import com.google.accompanist.insets.ProvideWindowInsets

enum class Screen {
    Home,
    Player
}

@Composable
fun ThePhonographApp() {
    // Props
    var screen by remember { mutableStateOf(Screen.Home) }

    // UI
    ThePhonographTheme {
        ProvideWindowInsets {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Layer 1:
                Home(
                    toPlayerScreen = { screen = Screen.Player },
                    backToHome = { screen = Screen.Home }
                )

                // Layer 2:
                if (screen == Screen.Player) {
                    Player()
                }

            } //: Box
        }
    }
}