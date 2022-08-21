package com.darealreally.thephonograph.data

import android.content.Context
import androidx.core.content.edit

/**
 * Tracks the last duration of song
 * by saving it to local storage
 */
class SongsLocalStorage(
    context: Context
) {

    private val sharedPref = context.getSharedPreferences(
        "Songs",
        Context.MODE_PRIVATE
    )


    fun saveSongDuration(
        songId: String,
        pausedDuration: Int // in seconds
    ) {
        sharedPref.edit {
            putInt(songId, pausedDuration)
            apply()
        }
    }

    fun getSongLastDuration(songId: String): Int {
       return sharedPref.getInt(songId, 0)
    }

}