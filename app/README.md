N-Queens Puzzle Android Application
This repository contains the source code for an Android application that implements the classic N-Queens puzzle. The user's goal is to place n queens on an n x n chessboard such that no two queens threaten each other.

Features
Dynamic Board Size: Select a board size n from 4x4 upwards.

Interactive Gameplay: Tap to place and remove queens on the board.

Real-time Validation: Instantly see conflicting placements highlighted.

Win Detection: A celebration screen appears when the puzzle is solved.

Local Leaderboards: Best times are saved locally for each board size.

Theming: Switch between light and dark themes.

Animations: Smooth animations for queen placement and a confetti celebration for victories.

How to Build and Run
Clone the Repository:

git clone <your-repository-url>

Open in Android Studio:

Open Android Studio (latest stable version recommended).

Select "Open" and navigate to the cloned project directory.

Allow Gradle to sync and download the necessary dependencies.

Run the Application:

Select an emulator or connect a physical Android device.

Click the "Run 'app'" button (green play icon) in the toolbar.

How to Test
The project includes both unit tests for the business logic and instrumented UI tests to verify user flows.

Running from Android Studio
This is the easiest way to run the tests.

Unit Tests:

In the Project view, navigate to app/src/test/java/.

Right-click on the package com.android.nqueensproblem.

Select "Run 'Tests in 'com.android.nqueensproblem''". These tests run quickly on your computer's JVM.

UI (Instrumentation) Tests:

In the Project view, navigate to app/src/androidTest/java/.

Right-click on the package com.android.nqueensproblem.

Select "Run 'Tests in 'com.android.nqueensproblem''". These tests will run on your connected emulator or device.

Running from the Command Line
Open a terminal in the project's root directory.

Unit Tests:

./gradlew testDebugUnitTest

UI (Instrumentation) Tests:

./gradlew connectedDebugAndroidTest

Architecture Decisions
The application is built using modern Android development practices, focusing on a clean, scalable, and testable architecture.

MVVM (Model-View-ViewModel)
The core architecture follows the MVVM pattern, which is Google's recommended approach for building UI-based applications.

Model: The game package (GameState.kt, Logic.kt). This layer contains the pure business logic and data structures for the N-Queens puzzle. It is written in pure Kotlin and has no dependency on the Android framework, making it highly portable and easy to unit test.

View: The ui package, built entirely with Jetpack Compose. The UI is a "dumb" layer that simply observes state changes from the ViewModel and forwards user input. It is responsible for rendering the chessboard, controls, and dialogs.

ViewModel: The viewmodel package (GameViewModel.kt, LeaderboardViewModel.kt). This layer acts as a bridge between the Model and the View. It holds the UI state (as StateFlow), processes user actions, uses the Model for calculations, and exposes the state for the UI to observe.

Clean Architecture Principles
The project structure also adheres to the principles of Clean Architecture, emphasizing a clear separation of concerns and a unidirectional dependency flow.

Dependency Rule: Dependencies flow inwards. The UI layer depends on the ViewModel, and the ViewModel depends on the Model (Logic). The core Model layer is completely independent.

Unidirectional Data Flow (UDF): The UI state is managed by the ViewModel and exposed as an immutable StateFlow. The UI observes this flow and can only trigger changes by calling functions on the ViewModel. This creates a predictable and easy-to-debug data flow: Event -> ViewModel -> State -> UI.

Repository Pattern: For data persistence (the leaderboard), a LeaderboardRepository is used to abstract the data source (Jetpack DataStore) from the rest of the app. This makes it easy to change the data storage mechanism in the future without affecting the ViewModels.

This architecture ensures the application is:

Testable: Each layer can be tested in isolation.

Maintainable: Code is organized by feature and responsibility, making it easy to understand and modify.

Scalable: New features can be added without requiring a major refactor of existing code.