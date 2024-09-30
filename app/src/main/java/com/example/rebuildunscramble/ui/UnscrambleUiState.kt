package com.example.rebuildunscramble.ui

data class UnscrambleUiState(
    val currentScrambleWord : String = "",
    val currentWordCount : Int = 0,
    val currentScore : Int = 0,
    val isGuessedWordWrong : Boolean = false,
    val isGameOver : Boolean = false
)
