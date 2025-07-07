package com.android.nqueensproblem.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "leaderboard")

class LeaderboardRepository(private val context: Context) {

    /**
     * Generates a dynamic DataStore key for each board size.
     * For example, "best_times_4", "best_times_5", etc.
     */
    private fun getBestTimesKey(boardSize: Int) = stringPreferencesKey("best_times_$boardSize")

    /**
     * Gets a flow of best times for a specific board size.
     * @param boardSize The board size for which to retrieve times.
     * @return A Flow emitting a sorted list of best times in milliseconds.
     */
    fun getBestTimes(boardSize: Int): Flow<List<Long>> = context.dataStore.data.map { preferences ->
        val timesString = preferences[getBestTimesKey(boardSize)] ?: ""
        if (timesString.isBlank()) {
            emptyList()
        } else {
            timesString.split(",").mapNotNull { it.toLongOrNull() }.sorted()
        }
    }

    /**
     * Adds a new time to the leaderboard for a specific board size.
     * @param boardSize The board size associated with the time.
     * @param timeInMillis The game duration to save.
     */
    suspend fun addTime(boardSize: Int, timeInMillis: Long) {
        val key = getBestTimesKey(boardSize)
        context.dataStore.edit { preferences ->
            val currentTimesString = preferences[key] ?: ""
            val newTimesString = if (currentTimesString.isBlank()) {
                timeInMillis.toString()
            } else {
                "$currentTimesString,$timeInMillis"
            }
            preferences[key] = newTimesString
        }
    }
}
