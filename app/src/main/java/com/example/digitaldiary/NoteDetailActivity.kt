package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.digitaldiary.model.Note
import com.example.digitaldiary.ui.theme.DigitalDiaryTheme
import com.example.digitaldiary.ui.AudioPlayer
import com.example.digitaldiary.viewmodel.NoteDetailViewModel
import com.example.digitaldiary.viewmodel.NoteDetailViewModelFactory

class NoteDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getStringExtra("noteId") ?: ""
        val viewModel: NoteDetailViewModel by viewModels {
            NoteDetailViewModelFactory(application, noteId)
        }
        setContent {
            val note by viewModel.note.observeAsState()
            DigitalDiaryTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    note?.let { NoteDetailScreen(it) }
                }
            }
        }
    }
}

@Composable
fun NoteDetailScreen(note: Note) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = note.text, style = MaterialTheme.typography.bodyLarge)
        note.latitude?.let { Text("Lat: ${'$'}it") }
        note.longitude?.let { Text("Lng: ${'$'}it") }
        note.imageUrl?.let { Text("Obraz: ${'$'}it") }
        note.audioUrl?.let {
            Text("Audio: ${'$'}it")
            AudioPlayer(it)
        }
    }
}
