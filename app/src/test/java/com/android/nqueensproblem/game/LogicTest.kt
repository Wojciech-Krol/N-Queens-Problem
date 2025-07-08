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
    fun `analyzeConflicts with one queen returns no conflicts`() {
        val queens = setOf(Position(0, 0))
        val analysis = logic.analyzeConflicts(queens)
        assertTrue(analysis.conflictingQueens.isEmpty())
        assertTrue(analysis.conflictPaths.isEmpty())
    }

    @Test
    fun `analyzeConflicts with two non-conflicting queens returns no conflicts`() {
        val queens = setOf(Position(0, 0), Position(1, 2))
        val analysis = logic.analyzeConflicts(queens)
        assertTrue(analysis.conflictingQueens.isEmpty())
        assertTrue(analysis.conflictPaths.isEmpty())
    }

    @Test
    fun `analyzeConflicts with two queens in a row returns correct conflicts`() {
        val queens = setOf(Position(0, 0), Position(0, 2))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.containsAll(queens))
        assertEquals(3, analysis.conflictPaths.size) // Path is (0,0), (0,1), (0,2)
        assertTrue(analysis.conflictPaths.contains(Position(0, 1)))
    }

    @Test
    fun `analyzeConflicts with two queens in a column returns correct conflicts`() {
        val queens = setOf(Position(1, 1), Position(3, 1))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.containsAll(queens))
        assertEquals(3, analysis.conflictPaths.size) // Path is (1,1), (2,1), (3,1)
        assertTrue(analysis.conflictPaths.contains(Position(2, 1)))
    }

    @Test
    fun `analyzeConflicts with two queens in a main diagonal returns correct conflicts`() {
        val queens = setOf(Position(0, 0), Position(2, 2))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.containsAll(queens))
        assertEquals(3, analysis.conflictPaths.size) // Path is (0,0), (1,1), (2,2)
        assertTrue(analysis.conflictPaths.contains(Position(1, 1)))
    }

    @Test
    fun `analyzeConflicts with two queens in an anti-diagonal returns correct conflicts`() {
        val queens = setOf(Position(0, 3), Position(2, 1))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(2, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.containsAll(queens))
        assertEquals(3, analysis.conflictPaths.size) // Path is (0,3), (1,2), (2,1)
        assertTrue(analysis.conflictPaths.contains(Position(1, 2)))
    }

    @Test
    fun `analyzeConflicts with three queens in a row marks all as conflicting`() {
        val queens = setOf(Position(2, 1), Position(2, 3), Position(2, 5))
        val analysis = logic.analyzeConflicts(queens)

        assertEquals(3, analysis.conflictingQueens.size)
        assertTrue(analysis.conflictingQueens.containsAll(queens))
        // Path should be from the outermost queens (2,1) to (2,5)
        assertEquals(5, analysis.conflictPaths.size)
    }

    @Test
    fun `checkWinCondition is true for valid 4x4 solution`() {
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

    @Test
    fun `checkWinCondition is false for full board with conflicts`() {
        val queens = setOf(Position(0, 0), Position(1, 1), Position(2, 2), Position(3, 3))
        val analysis = logic.analyzeConflicts(queens)

        assertFalse(logic.checkWinCondition(4, queens, analysis.conflictingQueens))
        assertFalse(analysis.conflictingQueens.isEmpty())
    }
}
