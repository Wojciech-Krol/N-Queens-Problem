package com.android.nqueensproblem.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.nqueensproblem.R
import com.android.nqueensproblem.game.GameState
import com.android.nqueensproblem.game.Position
import com.android.nqueensproblem.ui.theme.ChessComBlackSquare
import com.android.nqueensproblem.ui.theme.ChessComWhiteSquare

@Composable
fun Chessboard(
    gameState: GameState,
    onSquareTapped: (row: Int, col: Int) -> Unit
) {
    val lightSquare = ChessComWhiteSquare
    val darkSquare = ChessComBlackSquare

    Box(modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface)) {
        Column {
            for (row in 0 until gameState.boardSize) {
                Row {
                    for (col in 0 until gameState.boardSize) {
                        val position = Position(row, col)
                        val hasQueen = gameState.queens.contains(position)
                        val isConflictQueen = gameState.conflictingQueens.contains(position)
                        val isOnConflictPath = gameState.conflictPaths.contains(position)

                        val squareColor = if ((row + col) % 2 == 0) lightSquare else darkSquare

                        Square(
                            squareColor = squareColor,
                            hasQueen = hasQueen,
                            isConflictQueen = isConflictQueen,
                            isOnConflictPath = isOnConflictPath,
                            onClick = { onSquareTapped(row, col) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.Square(
    squareColor: Color,
    hasQueen: Boolean,
    isConflictQueen: Boolean,
    isOnConflictPath: Boolean,
    onClick: () -> Unit
) {
    val queenConflictColor = MaterialTheme.colorScheme.error
    val pathConflictColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)

    val queenScale by animateFloatAsState(
        targetValue = if (hasQueen) 0.9f else 0.0f,
        label = "Queen Scale Animation"
    )


    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .background(squareColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        if (isOnConflictPath) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(pathConflictColor)
            )
        }

        if (isConflictQueen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(queenConflictColor)
            )
        }

        if (queenScale > 0.01f) {
            Image(
                painter = painterResource(id = R.drawable.white_queen),
                contentDescription = "Queen",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(queenScale)
            )
        }
    }
}