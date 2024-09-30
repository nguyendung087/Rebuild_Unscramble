package com.example.rebuildunscramble.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rebuildunscramble.R
import com.example.rebuildunscramble.ui.theme.RebuildUnscrambleTheme
import com.example.rebuildunscramble.ui.theme.Shapes

@Composable
fun UnscrambleApp(
    unscrambleViewModel: UnscrambleViewModel = viewModel()
) {
    val unscrambleUiState = unscrambleViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            UnscrambleAppBar()
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UnscrambleLayout(
                currentScrambleWord = unscrambleUiState.value.currentScrambleWord,
                userGuess = unscrambleViewModel.userGuess,
                onValueChange = { unscrambleViewModel.updateUserGuess(it) },
                onKeyBoardDone = { unscrambleViewModel.checkUserGuess() },
                isGuessesWrong = unscrambleUiState.value.isGuessedWordWrong,
                wordCount = unscrambleUiState.value.currentWordCount
            )
            UnscrambleButton(
                onSubmit = { unscrambleViewModel.checkUserGuess() },
                onSkip = { unscrambleViewModel.skipWord() }
            )
            UnscrambleStatus(unscrambleUiState.value.currentScore)
            if (unscrambleUiState.value.isGameOver) {
                FinalDialog(
                    unscrambleUiState.value.currentScore,
                    onPlayAgain = { unscrambleViewModel.reset() }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnscrambleAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}

@Composable
private fun FinalDialog(
    score: Int,
    onPlayAgain : () -> Unit,
) {
    val activity = (LocalContext.current as Activity)
    AlertDialog(

        title = {
            Text(
                text = stringResource(R.string.congratulations)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.you_scored, score)
            )
        },
        onDismissRequest = {  },
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceTint),
                onClick = onPlayAgain
            ) {
                Text(
                    stringResource(R.string.play_again),
                    color = Color.White
                )
            }
        }
    )
}

@Composable
private fun UnscrambleLayout(
    currentScrambleWord: String,
    onValueChange: (String) -> Unit,
    userGuess: String,
    isGuessesWrong : Boolean,
    onKeyBoardDone : () -> Unit,
    wordCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        UnscrambleDisplay(
            currentScrambleWord = currentScrambleWord,
            onValueChange = onValueChange,
            userGuess = userGuess,
            isGuessesWrong = isGuessesWrong,
            onKeyBoardDone = onKeyBoardDone,
            wordCount = wordCount
        )
    }
}

@Composable
private fun UnscrambleStatus(
    score : Int
) {
    Card(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.score, score),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun UnscrambleDisplay(
    onValueChange : (String) -> Unit,
    userGuess : String,
    isGuessesWrong : Boolean,
    onKeyBoardDone : () -> Unit,
    currentScrambleWord : String,
    wordCount : Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.word_count, wordCount),
            modifier = Modifier
                .align(alignment = Alignment.End)
                .clip(Shapes.medium)
                .background(color = MaterialTheme.colorScheme.surfaceTint)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Text(
            text = currentScrambleWord,
            style = MaterialTheme.typography.displayMedium,

        )
        Text(
            text = stringResource(R.string.instructions),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = userGuess,
            shape = Shapes.large,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                if (isGuessesWrong) {
                    Text(stringResource(R.string.wrong_guess))
                } else {
                    Text(stringResource(R.string.enter_your_word))
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            ),
            isError = isGuessesWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyBoardDone() }
            )
        )
    }
}

@Composable
private fun UnscrambleButton(
    onSubmit : () -> Unit,
    onSkip : () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit
        ) {
            Text(
                text = stringResource(R.string.submit),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSkip
        ) {
            Text(
                text = stringResource(R.string.skip),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    RebuildUnscrambleTheme {
        UnscrambleApp()
    }
}