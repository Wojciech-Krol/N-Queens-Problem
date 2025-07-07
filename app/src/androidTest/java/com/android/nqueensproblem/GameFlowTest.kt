package com.android.nqueensproblem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class GameFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startGame() {
        composeTestRule.onNodeWithText("Start Game").performClick()

        composeTestRule.onNodeWithText("N-Queens Puzzle").assertIsDisplayed()
    }

    @Test
    fun navigateToLeaderboardAndBack() {
        composeTestRule.onNodeWithText("View Leaderboard").performClick()

        composeTestRule.onNodeWithText("Best Times").assertIsDisplayed()
    }
}
