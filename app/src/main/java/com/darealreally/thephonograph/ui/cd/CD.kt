package com.darealreally.thephonograph.ui.cd

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darealreally.thephonograph.R
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme

@Composable
fun CD(
    modifier: Modifier = Modifier,
    scale: Float = 0.5F,
    imageId: Int? = null,
    spin: Boolean = false
) {
    // Props
    val image =
        if (imageId == null) null
        else ImageBitmap.imageResource(id = imageId)

    // Animation
    val degrees = remember { Animatable(0F) }

    LaunchedEffect(spin) {
        if (!spin) {
            // back to initial value and stops animation
            degrees.snapTo(0F)
        } else {
            degrees.animateTo(
                targetValue = 360F,
                animationSpec =  infiniteRepeatable(
                    animation = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    // UI
    Box(
        modifier = modifier.rotate(degrees.value),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1:
        DonutShape(
            size = 117.32F * scale,
            holeSize = 75.21F * scale
        )
        // Layer 2:
        DonutShape(
            size = 202.85F * scale,
            holeSize = 125.58F * scale
        )
        // Layer 3:
        DonutShape(
            size = 666.51F * scale,
            holeSize = 224.19F * scale,
            image = image
        )
        // Layer 4:
        Box(
            modifier = Modifier
                .size((696.8 * scale).dp)
                .border(
                    width = (8 * scale).dp,
                    shape = CircleShape,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.2F)
                )
        )
    }
}

/**
 * Preview Section
 */
@Preview(name = "Shape w/o Image")
@Composable
fun CDPreview() {
    ThePhonographTheme {
        CD()
    }
}

@Preview(name = "Shape w/ Image")
@Composable
fun CD2Preview() {
    ThePhonographTheme {
        CD(
            imageId = R.drawable.ineverdie,
            spin = true
        )
    }
}