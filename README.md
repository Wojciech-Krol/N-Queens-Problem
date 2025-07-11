# N-Queens Puzzle Android Application

This repository contains the source code for an Android application that implements the N-Queens puzzle. The user's goal is to place `n` queens on an `n x n` chessboard such that no two queens threaten each other.

## Features

* **Dynamic Board Size:** Select a board size `n` from 4x4 upwards to 14x14.
* **Interactive Gameplay:** Tap to place and remove queens on the board.
* **Real-time Validation:** Instantly see conflicting placements highlighted.
* **Win Detection:** A celebration screen appears when the puzzle is solved.
* **Local Leaderboards:** Best times are saved locally for each board size.
* **Animations:** Smooth animations for queen placement and a confetti celebration for victories.

## How to Build and Run

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Wojciech-Krol/N-Queens-Problem
    ```

2.  **Open in Android Studio:**
    * Open Android Studio (latest stable version recommended).
    * Select "Open" and navigate to the cloned project directory.
    * Allow Gradle to sync and download the necessary dependencies.

3.  **Run the Application:**
    * Select an emulator or connect a physical Android device.
    * Click the "Run 'app'" button (green play icon) in the toolbar.

## How to Test

The project includes unit tests for the business logic

### Running from Android Studio

This is the easiest way to run the tests.

1.  **Unit Tests:**
    * In the Project view, navigate to `app/src/test/java/`.
    * Right-click on the package `com.android.nqueensproblem`.
    * Select **"Run 'Tests in 'com.android.nqueensproblem''"**. These tests run quickly on your computer's JVM.

### Running from the Command Line

Open a terminal in the project's root directory.

1.  **Unit Tests:**
    ```bash
    ./gradlew testDebugUnitTest
    ```

## Architecture Decisions

The application is built using modern Android development practices, focusing on a clean, scalable, and testable architecture.

### MVVM (Model-View-ViewModel)

The core architecture follows the MVVM pattern, which is Google's recommended approach for building UI-based applications.

* **Model:** The `game` package (`GameState.kt`, `Logic.kt`). This layer contains the pure business logic and data structures for the N-Queens puzzle. It is written in pure Kotlin and has no dependency on the Android framework, making it highly portable and easy to unit test.
* **View:** The `ui` package, built entirely with **Jetpack Compose**. The UI is a "dumb" layer that simply observes state changes from the ViewModel and forwards user input. It is responsible for rendering the chessboard, controls, and dialogs.
* **ViewModel:** The `viewmodel` package (`GameViewModel.kt`, `LeaderboardViewModel.kt`). This layer acts as a bridge between the Model and the View. It holds the UI state (as `StateFlow`), processes user actions, uses the Model for calculations, and exposes the state for the UI to observe.
