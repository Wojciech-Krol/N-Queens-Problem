package com.android.nqueensproblem.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.nqueensproblem.R
import com.android.nqueensproblem.ui.component.StyledButton
import kotlin.math.roundToInt

@Composable
fun SetupScreen(
    onStartGame: (boardSize: Int) -> Unit,
    onShowLeaderboard: () -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(4f) }
    val boardSize = sliderPosition.roundToInt()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp)
                .scale(0.7f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.white_queen),
                contentDescription = "Queen",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
            text = "N-Queens Puzzle",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Board Size: $boardSize x $boardSize",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 4f..14f,
            steps = 9 // (14 - 4) - 1 = 9 steps between values
        )
        Spacer(modifier = Modifier.height(32.dp))

        StyledButton(onClick = onShowLeaderboard) {
            Text("View Leaderboard")
        }
        Spacer(modifier = Modifier.height(16.dp))

        StyledButton(
            onClick = { onStartGame(boardSize) },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Start Game", style = MaterialTheme.typography.titleMedium)
        }
    }
}