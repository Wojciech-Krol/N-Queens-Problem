package com.android.nqueensproblem.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameControls(onReset: () -> Unit) {
    StyledButton(onClick = onReset) {
        Text("Reset Game")
    }
}