package com.android.nqueensproblem.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

// Made a separate composable for the win dialog, instead of a buitlin dialog.
// This allows for more customization and flexibility in the UI.
@Composable
fun WinOverlay(
    winTimeMillis: Long?,
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
                StyledButton(onClick = onNavigateBackToSetup) {
                    Text("New Game")
                }
            }
        }
    }
}


private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val milliseconds = millis % 1000
    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds)
}