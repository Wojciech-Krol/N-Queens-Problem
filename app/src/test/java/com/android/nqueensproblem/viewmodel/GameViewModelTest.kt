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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
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

        testDispatcher.scheduler.advanceUntilIdle()

        val queens = viewModel.gameState.value.queens
        assertTrue(queens.contains(Position(0, 0)))
        assertEquals(1, queens.size)
    }

    @Test
    fun `onSquareTapped removes a queen from an occupied square`() = runTest {
        viewModel.resetGame(4)
        viewModel.onSquareTapped(0, 0)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSquareTapped(0, 0)
        testDispatcher.scheduler.advanceUntilIdle()

        val queens = viewModel.gameState.value.queens
        assertTrue(queens.isEmpty())
    }
    
}
