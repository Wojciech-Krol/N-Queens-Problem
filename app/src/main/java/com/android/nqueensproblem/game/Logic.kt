package com.android.nqueensproblem.game

data class ConflictAnalysis(
    val conflictingQueens: Set<Position>,
    val conflictPaths: Set<Position>
)

class Logic {

    /**
     * Analyzes the board to find all conflicting queens and their lines of sight.
     * This version highlights only the path segment between two conflicting queens.
     * @param queens The set of all currently placed queens.
     * @return A ConflictAnalysis object containing the conflicting queens and paths.
     */
    fun analyzeConflicts(queens: Set<Position>): ConflictAnalysis {
        val conflictingQueens = mutableSetOf<Position>()
        val conflictPaths = mutableSetOf<Position>()
        if (queens.size < 2) return ConflictAnalysis(emptySet(), emptySet())

        val queenList = queens.toList()

        for (i in queenList.indices) {
            for (j in (i + 1) until queenList.size) {
                val q1 = queenList[i]
                val q2 = queenList[j]

                // Check for row conflict
                if (q1.row == q2.row) {
                    conflictingQueens.add(q1)
                    conflictingQueens.add(q2)
                    // Highlight only the squares between the two queens on the same row
                    for (col in minOf(q1.col, q2.col)..maxOf(q1.col, q2.col)) {
                        conflictPaths.add(Position(q1.row, col))
                    }
                }

                // Check for column conflict
                if (q1.col == q2.col) {
                    conflictingQueens.add(q1)
                    conflictingQueens.add(q2)
                    // Highlight only the squares between the two queens on the same column
                    for (row in minOf(q1.row, q2.row)..maxOf(q1.row, q2.row)) {
                        conflictPaths.add(Position(row, q1.col))
                    }
                }

                // Check for diagonal conflict
                if (kotlin.math.abs(q1.row - q2.row) == kotlin.math.abs(q1.col - q2.col)) {
                    conflictingQueens.add(q1)
                    conflictingQueens.add(q2)

                    // Determine the "smaller" queen (top-most) to start the path from
                    val startQueen = if (q1.row < q2.row) q1 else q2
                    val endQueen = if (q1.row < q2.row) q2 else q1

                    // Determine the column direction (1 for right, -1 for left)
                    val colStep = if (endQueen.col > startQueen.col) 1 else -1

                    // Iterate along the diagonal path between the two queens
                    for (step in 0..(endQueen.row - startQueen.row)) {
                        val row = startQueen.row + step
                        val col = startQueen.col + (step * colStep)
                        conflictPaths.add(Position(row, col))
                    }
                }
            }
        }
        return ConflictAnalysis(conflictingQueens, conflictPaths)
    }

    /**
     * Checks if the current state represents a win.
     * A win occurs when there are n queens on the board and no conflicts.
     * @param boardSize The size of the board (n).
     * @param queens The set of all currently placed queens.
     * @param conflicts The set of conflicting queens.
     * @return True if the win condition is met, false otherwise.
     */
    fun checkWinCondition(boardSize: Int, queens: Set<Position>, conflicts: Set<Position>): Boolean {
        return queens.size == boardSize && conflicts.isEmpty()
    }
}