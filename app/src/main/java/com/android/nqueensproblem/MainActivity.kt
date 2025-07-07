package com.android.nqueensproblem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.android.nqueensproblem.ui.screen.GameScreen
import com.android.nqueensproblem.ui.screen.SetupScreen
import com.android.nqueensproblem.ui.theme.NQueensProblemTheme
import com.android.nqueensproblem.ui.screen.LeaderboardScreen

sealed class Screen {
    data object Setup : Screen()
    data class Game(val boardSize: Int) : Screen()
    data object Leaderboard : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            NQueensProblemTheme {
                var currentScreen: Screen by remember { mutableStateOf(Screen.Setup) }

                when (val screen = currentScreen) {
                    is Screen.Setup -> {
                        SetupScreen(

                            onStartGame = { boardSize ->
                                currentScreen = Screen.Game(boardSize)
                            },
                            onShowLeaderboard = {
                                currentScreen = Screen.Leaderboard
                            }
                        )
                    }
                    is Screen.Game -> {
                        GameScreen(
                            boardSize = screen.boardSize,
                            onNavigateBack = { currentScreen = Screen.Setup }
                        )
                    }
                    is Screen.Leaderboard -> {
                        LeaderboardScreen(
                            onNavigateBack = { currentScreen = Screen.Setup }
                        )
                    }
                }
            }
        }
    }
}
