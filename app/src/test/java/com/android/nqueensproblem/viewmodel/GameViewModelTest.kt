import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.nqueensproblem.game.Position
import com.android.nqueensproblem.viewmodel.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    // This rule swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // We need to mock the Application context for the AndroidViewModel
        val mockApplication = mock(Application::class.java)
        viewModel = GameViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSquareTapped places a queen on an empty square`() = runTest {
        viewModel.resetGame(4)
        viewModel.onSquareTapped(0, 0)

        // Advance the dispatcher to allow the coroutine to complete
        testDispatcher.scheduler.advanceUntilIdle()

        val queens = viewModel.gameState.value.queens
        assertTrue(queens.contains(Position(0, 0)))
        assertEquals(1, queens.size)
    }

    @Test
    fun `onSquareTapped removes a queen from an occupied square`() = runTest {
        viewModel.resetGame(4)
        viewModel.onSquareTapped(0, 0) // Place queen
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSquareTapped(0, 0) // Remove queen
        testDispatcher.scheduler.advanceUntilIdle()

        val queens = viewModel.gameState.value.queens
        assertTrue(queens.isEmpty())
    }

    @Test
    fun `win condition updates state correctly`() = runTest {
        viewModel.resetGame(4)

        // Place a winning combination
        viewModel.onSquareTapped(0, 1)
        viewModel.onSquareTapped(1, 3)
        viewModel.onSquareTapped(2, 0)
        viewModel.onSquareTapped(3, 2)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.gameState.value.isWin)
        // Check if confetti party was created
        assertFalse(viewModel.gameState.value.parties.isEmpty())
    }
}
