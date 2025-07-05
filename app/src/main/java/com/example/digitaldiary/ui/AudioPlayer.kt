package com.example.digitaldiary.ui

import android.media.MediaPlayer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import java.io.IOException

@Composable
fun AudioPlayer(path: String) {
    var isPlaying by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()
            } catch (e: IOException) {
                // ignore
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    Button(onClick = {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying
    }) {
        Text(if (isPlaying) "Pauza" else "Odtwórz")
    }
}
