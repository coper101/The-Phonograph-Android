package com.darealreally.thephonograph.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.savedstate.SavedStateRegistryOwner
import com.darealreally.thephonograph.data.Song
import com.darealreally.thephonograph.data.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HomeState(
    val songs: List<Song> = emptyList(),
    val songPlaying: Song? = null,
    val selectedSong: Song? = null,
) {
    val isSongPlaying: Boolean
        get() = songPlaying != null

    val top10Songs: List<Song>
        get() = songs.sortedBy { it.rankNumber }

    val latestReleasedSongs: List<Song>
        get() = songs.sortedBy { it.releasedDate }
}

class HomeViewModel(
    private val songRepository: SongRepository
) : ViewModel() {

    private val songPlaying = MutableStateFlow<Song?>(null)
    private val selectedSong = MutableStateFlow<Song?>(null)

    // mutable, to be accessible only in this class)
    private val _state = MutableStateFlow(HomeState())

    // non-mutable version to be accessible by UI
    val state: StateFlow<HomeState>
        get() = _state

    init {
        // gets the latest values from flows and updates the ui state
        viewModelScope.launch {
            combine(
                songPlaying,
                selectedSong,
                songRepository.songs
            ) { songPlaying, selectedSong, songs ->
                // return a new instance state every time when
                // there is a change in one of the values
                // like redux in react
                HomeState(
                    songs = songs,
                    songPlaying = songPlaying,
                    selectedSong = selectedSong
                )
            }.collect {
                _state.value = it
            }
        }
    }

    // factory allow us to pass a dependency of this view model
    // since view model is generic "viewModel()"
    companion object {
        fun provideFactory(
            context: Context
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(SongRepository(context))
            }
        }
    }

    fun onChangeSongPlaying(song: Song?) {
        songPlaying.value = song
        Log.d("HomeVM", "songPlaying: ${_state.value.songPlaying}")
    }

    fun onSelectSong(song: Song?) {
        selectedSong.value = song
        Log.d("HomeVM", "selectedSong: ${_state.value.selectedSong}")
    }

}