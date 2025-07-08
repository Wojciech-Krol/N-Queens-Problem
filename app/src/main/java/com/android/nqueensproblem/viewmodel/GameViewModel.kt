package com.android.nqueensproblem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.nqueensproblem.data.LeaderboardRepository
import com.android.nqueensproblem.game.GameState
import com.android.nqueensproblem.game.Logic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameLogic = Logic()
    private val leaderboardRepository = LeaderboardRepository(application)
    private var gameStartTime: Long = 0L

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun onSquareTapped(row: Int, col: Int) {
        if (_gameState.value.isWin) return

        viewModelScope.launch {
            val tappedPosition = com.android.nqueensproblem.game.Position(row, col)
            val currentQueens = _gameState.value.queens
            val newQueens = if (currentQueens.contains(tappedPosition)) {
                currentQueens - tappedPosition
            } else {
                currentQueens + tappedPosition
            }
            updateGameState(newQueens)
        }
    }

    fun resetGame(boardSize: Int) {
        viewModelScope.launch {
            _gameState.value = GameState(boardSize = boardSize)
            gameStartTime = System.currentTimeMillis()
        }
    }

    fun onConfettiFinished() {
        _gameState.update { it.copy(parties = emptyList()) }
    }

    private fun updateGameState(newQueens: Set<com.android.nqueensproblem.game.Position>) {
        val boardSize = _gameState.value.boardSize
        val conflictAnalysis = gameLogic.analyzeConflicts(newQueens)
        val isWin = gameLogic.checkWinCondition(boardSize, newQueens, conflictAnalysis.conflictingQueens)

        _gameState.update { currentState ->
            val gameDuration = System.currentTimeMillis() - gameStartTime
            val parties = if (isWin && !currentState.isWin) {
                viewModelScope.launch {
                    leaderboardRepository.addTime(boardSize, gameDuration)
                }
                listOf(
                    Party(
                        speed = 10f,
                        maxSpeed = 40f,
                        damping = 0.85f,
                        spread = Spread.WIDE,
                        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100),
                        position = nl.dionsegijn.konfetti.core.Position.Relative(0.5, 0.0),
                        angle = Angle.BOTTOM
                    )
                )
            } else {
                currentState.parties
            }

            currentState.copy(
                queens = newQueens,
                conflictingQueens = conflictAnalysis.conflictingQueens,
                conflictPaths = conflictAnalysis.conflictPaths,
                isWin = isWin,
                parties = parties,
                gameDuration = gameDuration
            )
        }
    }
}
