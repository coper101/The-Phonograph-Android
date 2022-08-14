package com.darealreally.thephonograph.data

import java.util.*

data class Song(
    var id: String,
    val title: String,
    val singer: String,
    val albumArtName: String,
    val rankNumber: Int,
    val lastPosDiff: Int,
    val duration: Int,
    val releasedDate: Date,
    val lyrics: String
) {
    val lyricsByLine: List<String>
        get() = lyrics.split("\n")
}