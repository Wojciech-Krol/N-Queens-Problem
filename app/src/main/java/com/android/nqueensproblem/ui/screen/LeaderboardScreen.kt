package com.android.nqueensproblem.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.nqueensproblem.viewmodel.LeaderboardViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    leaderboardViewModel: LeaderboardViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val bestTimes by leaderboardViewModel.bestTimes.collectAsState()
    val selectedBoardSize by leaderboardViewModel.selectedBoardSize.collectAsState()
    val boardSizes = (4..14).toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Best Times") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                boardSizes.forEach { size ->
                    FilterChip(
                        selected = (size == selectedBoardSize),
                        onClick = { leaderboardViewModel.onBoardSizeSelected(size) },
                        label = { Text("${size}x${size}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                    ))
                }
            }

            if (bestTimes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No best times yet for ${selectedBoardSize}x${selectedBoardSize}.", fontSize = 18.sp, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(bestTimes) { index, time ->
                        BestTimeEntry(rank = index + 1, timeInMillis = time)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun BestTimeEntry(rank: Int, timeInMillis: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = formatTime(timeInMillis),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// Helper function to format milliseconds into a readable string (MM:SS.ms)
private fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val milliseconds = millis % 1000
    return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds)
}
