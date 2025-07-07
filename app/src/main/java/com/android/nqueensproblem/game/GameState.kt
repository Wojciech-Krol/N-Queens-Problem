package com.android.nqueensproblem.game

import nl.dionsegijn.konfetti.core.Party

data class Position(val row: Int, val col: Int)

/**
 * Represents the complete, immutable state of the N-Queens game.
 * @param parties A list of Party objects for the KonfettiView to render.
 */
data class GameState(
    val boardSize: Int = 4,
    val queens: Set<Position> = emptySet(),
    val conflictingQueens: Set<Position> = emptySet(),
    val conflictPaths: Set<Position> = emptySet(),
    val isWin: Boolean = false,
    val parties: List<Party> = emptyList(),
    val gameDuration: Long = 0L
)
