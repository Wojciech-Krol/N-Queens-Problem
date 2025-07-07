package com.android.nqueensproblem.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


//Was a feature before adding WinOverlay, now unused
@Composable
fun WinDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Congratulations!") },
        text = { Text("You solved the puzzle!") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Play Again")
            }
        }
    )
}