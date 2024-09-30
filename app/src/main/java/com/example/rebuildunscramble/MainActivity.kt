package com.example.rebuildunscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.rebuildunscramble.ui.UnscrambleApp
import com.example.rebuildunscramble.ui.theme.RebuildUnscrambleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RebuildUnscrambleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    UnscrambleApp()
                }
            }
        }
    }
}