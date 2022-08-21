package com.darealreally.thephonograph.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darealreally.thephonograph.data.Song
import com.darealreally.thephonograph.data.SongRepository
import com.darealreally.thephonograph.data.SongsLocalStorage
import com.darealreally.thephonograph.data.Timer
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val songs: List<Song> = emptyList(),
    val songPlaying: Song? = null,
    val selectedSong: Song? = null,
    val songPlayingTime: Int = 0
) {
    val isSongPlaying: Boolean
        get() = songPlaying != null

    val top10Songs: List<Song>
        get() = songs.sortedBy { it.rankNumber }

    val latestReleasedSongs: List<Song>
        get() = songs.sortedBy { it.releasedDate }
}

class HomeViewModel(
    private val songRepository: SongRepository,
    private val songsLocalStorage: SongsLocalStorage
) : ViewModel() {

    // Song
    private val songPlaying = MutableStateFlow<Song?>(null)
    private val selectedSong = MutableStateFlow<Song?>(null)

    // Timer
    private var timerJob: Job? = null
    private val songPlayingTime = MutableStateFlow(0)

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
                songPlayingTime,
                songRepository.songs
            ) { songPlaying, selectedSong, songPlayingTime, songs ->
                // return a new instance state every time when
                // there is a change in one of the values
                // like redux in react
                HomeState(
                    songs = songs,
                    songPlaying = songPlaying,
                    selectedSong = selectedSong,
                    songPlayingTime = songPlayingTime
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
                HomeViewModel(
                    SongRepository(context),
                    SongsLocalStorage(context)
                )
            }
        }
    }

    fun onChangeSongPlaying(song: Song?) {
        // Save last duration
        val currentSong = songPlaying.value
        if (currentSong != null) {
            Log.d("HomeVM", "saved song duration: $currentSong")
            songsLocalStorage.saveSongDuration(
                currentSong.id,
                songPlayingTime.value
            )
        }

        // Prevent player from going down (disappearing)
        if (song == null) {
            selectedSong.value = currentSong
        }

        // Set new song to play or pause current song
        songPlaying.value = song
        Log.d("HomeVM", "new songPlaying: ${_state.value.songPlaying}")

        // Playing - get last duration & start timer
        val newSong = songPlaying.value
        if (newSong != null) {
            startTimer(
                from = songsLocalStorage.getSongLastDuration(newSong.id),
                to = newSong.duration
            )
            return
        }
        // Paused - cancel timer
        stopTimer()
    }

    private fun startTimer(
        from: Int,
        to: Int
    ) {
        Log.d("HomeVM", "startTimer")

        // cancel previous timer
        stopTimer()
        songPlayingTime.value = from

        // start fresh timer
        timerJob = viewModelScope.launch {
            Timer.timer(
                if (from == to) 0 else from, // start from 0
                to
            )
                .collectLatest { sec ->
                    songPlayingTime.value = sec
                    // stop playing if it reaches end of song
                    if (sec == to) {
                        stopTimer()
                    }
                }
        }
    }

    private fun stopTimer() {
        Log.d("HomeVM", "cancelTimer")
        timerJob?.cancel()
        timerJob = null
    }

    fun onSelectSong(song: Song?) {
        selectedSong.value = song
        Log.d("HomeVM", "selectedSong: ${_state.value.selectedSong}")
    }

}