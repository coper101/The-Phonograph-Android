package com.darealreally.thephonograph.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class Timer {
    companion object {
        fun timer(
            fromSec: Int,
            toSec: Int
        ): Flow<Int> {
            return (fromSec..toSec)
                .asFlow()
                .onEach { delay(1_000) }
        }
    }
}
