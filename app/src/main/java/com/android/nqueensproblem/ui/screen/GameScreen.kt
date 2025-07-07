package com.android.nqueensproblem.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.nqueensproblem.game.GameState
import com.android.nqueensproblem.ui.component.Chessboard
import com.android.nqueensproblem.ui.component.GameControls
import com.android.nqueensproblem.viewmodel.GameViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    boardSize: Int,
    gameViewModel: GameViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val gameState by gameViewModel.gameState.collectAsStateWithLifecycle()

    LaunchedEffect(boardSize) {
        gameViewModel.resetGame(boardSize)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("N-Queens Puzzle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Setup")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GameInfo(gameState = gameState)
                Spacer(modifier = Modifier.height(24.dp))
                Chessboard(
                    gameState = gameState,
                    onSquareTapped = { row, col -> gameViewModel.onSquareTapped(row, col) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                GameControls(onReset = { gameViewModel.resetGame(boardSize) })
            }
            }

        AnimatedVisibility(
                visible = gameState.isWin,
                enter = scaleIn(),
                exit = scaleOut()
        ) {
            WinOverlay(
                winTimeMillis = gameState.gameDuration,
                onNavigateBackToSetup = onNavigateBack
            )
        }
        key(gameState.parties) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = gameState.parties,
                updateListener = remember {
                    object : OnParticleSystemUpdateListener {
                        override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
                            if (activeSystems == 0) {
                                gameViewModel.onConfettiFinished()
                            }
                        }
                    }
                }
            )
        }

    }
    }


// Made a separate composable for the win dialog, instead of a buitlin dialog.
// This allows for more customization and flexibility in the UI.
@Composable
private fun WinOverlay(
    winTimeMillis: Long?, // New parameter to accept the time
    onNavigateBackToSetup: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Congratulations!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "You solved the puzzle!",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (winTimeMillis != null) {
                    Text(
                        text = "Time: ${formatTime(winTimeMillis)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(onClick = onNavigateBackToSetup) {
                    Text("New Game")
                }
            }
        }
    }
}

@Composable
private fun GameInfo(gameState: GameState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Board Size: ${gameState.boardSize}x${gameState.boardSize}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Queens Left: ${gameState.boardSize - gameState.queens.size}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val milliseconds = millis % 1000
    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds)
}
