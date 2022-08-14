package com.darealreally.thephonograph.data

import android.content.Context
import com.darealreally.thephonograph.utils.load
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository<T> {
    fun loadItems(): List<T>
}

class SongRepository(
    private val context: Context
): Repository<Song> {

    val songs: Flow<List<Song>> = flow {
        val items = loadItems()
        emit(items)
    }

    override fun loadItems(): List<Song> {
        return load(context, "Songs.json")
    }
}