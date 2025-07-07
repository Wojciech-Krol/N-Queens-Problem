package com.android.nqueensproblem.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.nqueensproblem.R
import com.android.nqueensproblem.game.GameState
import com.android.nqueensproblem.ui.component.Chessboard
import com.android.nqueensproblem.ui.component.GameControls
import com.android.nqueensproblem.ui.component.WinOverlay
import com.android.nqueensproblem.viewmodel.GameViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem


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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Setup", tint = MaterialTheme.colorScheme.onPrimary)
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
@Composable
private fun GameInfo(gameState: GameState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Board Size: ${gameState.boardSize}x${gameState.boardSize}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.white_queen),
                contentDescription = "Queens Left Image",
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = "x${gameState.boardSize - gameState.queens.size}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

