package com.android.nqueensproblem.game

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LogicTest {

    private lateinit var logic: Logic

    @Before
    fun setUp() {
        logic = Logic()
    }

    @Test
    fun `analyzeConflicts with no queens returns no conflicts`() {
        val analysis = logic.analyzeConflicts(emptySet())
        assertTrue(analysis.conflictingQueens.isEmpty())
        assertTrue(analysis.conflictPaths.isEmpty())
    }

    @Test
    fun `analyzeConflicts with two queens in a row returns correct conflicts`() {
        val queens = setOf(Position(0, 0), Position(0, 2))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.contains(Position(0, 0)))
        assertTrue(analysis.conflictingQueens.contains(Position(0, 2)))

        // Path should be (0,0), (0,1), (0,2)
        assertEquals(3, analysis.conflictPaths.size)
        assertTrue(analysis.conflictPaths.contains(Position(0, 1)))
    }

    @Test
    fun `analyzeConflicts with two queens in a diagonal returns correct conflicts`() {
        val queens = setOf(Position(0, 0), Position(2, 2))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.contains(Position(0, 0)))
        assertTrue(analysis.conflictingQueens.contains(Position(2, 2)))

        // Path should be (0,0), (1,1), (2,2)
        assertEquals(3, analysis.conflictPaths.size)
        assertTrue(analysis.conflictPaths.contains(Position(1, 1)))
    }

    @Test
    fun `checkWinCondition is true for valid solution`() {
        // A known valid solution for a 4x4 board
        val solution = setOf(Position(0, 1), Position(1, 3), Position(2, 0), Position(3, 2))
        val analysis = logic.analyzeConflicts(solution)

        assertTrue(logic.checkWinCondition(4, solution, analysis.conflictingQueens))
    }

    @Test
    fun `checkWinCondition is false for incomplete board`() {
        val queens = setOf(Position(0, 1), Position(1, 3))
        val analysis = logic.analyzeConflicts(queens)

        assertFalse(logic.checkWinCondition(4, queens, analysis.conflictingQueens))
    }
}