package com.example.rebuildunscramble.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rebuildunscramble.data.MAX_NO_OF_WORDS
import com.example.rebuildunscramble.data.SCORE_INCREASE
import com.example.rebuildunscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UnscrambleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UnscrambleUiState())
    val uiState : StateFlow<UnscrambleUiState> = _uiState.asStateFlow()

    private var usedWords : MutableSet<String> = mutableSetOf()
    private var currentScrambleWord = ""

    var userGuess by mutableStateOf("")
        private set

    fun skipWord() {
        updateUiState(_uiState.value.currentScore)
        updateUserGuess("")
    }

    fun updateUserGuess(guessedWord : String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if(userGuess.equals(currentScrambleWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.currentScore.plus(SCORE_INCREASE)
            updateUiState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = true
                )
            }
        }
        updateUserGuess("")
    }

    fun reset() {
        usedWords.clear()
        _uiState.value = UnscrambleUiState(
            currentScrambleWord = pickRandomWordAndShuffle()
        )
    }

    init {
        reset()
    }

    private fun pickRandomWordAndShuffle() : String {
        currentScrambleWord = allWords.random()
        return if(usedWords.contains(currentScrambleWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentScrambleWord)
            shuffleWord(currentScrambleWord)
        }

    }

    private fun shuffleWord(word : String) : String {
        var tempWord = word.toCharArray()
        tempWord.shuffle()
        if(String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun updateUiState(updatedScore : Int) {
        if(_uiState.value.currentWordCount == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentScore = updatedScore,
                    isGuessedWordWrong = false,
                    isGameOver = true,
                    currentScrambleWord = pickRandomWordAndShuffle()
                )
            }
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(
                    currentScrambleWord = currentScrambleWord,
                    currentScore = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }

    }
}