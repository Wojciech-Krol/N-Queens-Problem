package com.android.nqueensproblem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.nqueensproblem.data.LeaderboardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn


class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {

    private val leaderboardRepository = LeaderboardRepository(application)

    private val _selectedBoardSize = MutableStateFlow(4)

    val selectedBoardSize: StateFlow<Int> = _selectedBoardSize.asStateFlow()

    /**
     * Exposes the best times for the currently selected board size
     * It uses `flatMapLatest` to automatically switch to the new data flow
     * whenever `selectedBoardSize` changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val bestTimes: StateFlow<List<Long>> = _selectedBoardSize.flatMapLatest { boardSize ->
        leaderboardRepository.getBestTimes(boardSize)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onBoardSizeSelected(boardSize: Int) {
        _selectedBoardSize.value = boardSize
    }
}
