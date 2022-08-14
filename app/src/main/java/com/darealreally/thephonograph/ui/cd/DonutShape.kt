package com.darealreally.thephonograph.ui.cd

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.darealreally.thephonograph.R
import com.darealreally.thephonograph.ui.theme.ThePhonographTheme

@Composable
fun DonutShape(
    modifier: Modifier = Modifier,
    size: Float = 666.51F / 2,
    holeSize: Float = 224.19F / 2,
    image: ImageBitmap? = null,
    alpha: Float = 0.2F
) {
    val onBackground = MaterialTheme.colors.onBackground

    Canvas(
        modifier = Modifier
            .requiredSize(size.dp)
            .then(modifier)
    ) {
        val (cWidth, cHeight) = this.size
        val hole = holeSize.dp.toPx()

        val outer = Path().apply {
            addOval(
                Rect(
                    Offset.Zero,
                    (this@Canvas).size
                )
            )
            fillType = PathFillType.EvenOdd
        }

        val space = (cWidth - hole) / 2
        val inner = Path().apply {
            addOval(
                Rect(
                    topLeft = Offset(
                        x = space,
                        y = space
                    ),
                    bottomRight = Offset(
                        x = hole + space,
                        y = hole + space
                    )
                )
            )
        }
        outer.addPath(inner)

        // CD With Image
        image?.let {

            // maintain aspect ratio when resizing
            var bitmap = image.asAndroidBitmap()
            val scale = bitmap.width / bitmap.height
            val newHeight = cWidth * scale

            bitmap = Bitmap.createScaledBitmap(
                bitmap,
                cWidth.toInt(),
                newHeight.toInt(),
                false
            )

            clipPath(
                path = outer,
                clipOp = ClipOp.Intersect
            ) {
                drawImage(image =  bitmap.asImageBitmap())
            }
            return@Canvas
        }

        // CD Without Image
        drawPath(
            path = outer,
            color = onBackground.copy(alpha = alpha)
        )
    }
}

/**
 * Preview Section
 */
@Preview(name = "Shape w/o Image")
@Composable
fun DonutShapePreview() {
    ThePhonographTheme {
        DonutShape()
    }
}

@Preview(name = "Shape w/ Image")
@Composable
fun DonutShape2Preview() {
    ThePhonographTheme {
        DonutShape(
            image = ImageBitmap.imageResource(id = R.drawable.ineverdie)
        )
    }
}