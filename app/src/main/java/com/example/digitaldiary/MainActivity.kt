package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.digitaldiary.ui.theme.DigitalDiaryTheme
import com.example.digitaldiary.viewmodel.MainViewModel
import com.example.digitaldiary.viewmodel.MainViewModelFactory
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Application

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalDiaryTheme {
                Scaffold(
                    content = { innerPadding ->
                        MainScreen(viewModel, Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val location by viewModel.location.observeAsState()
    val imageUrl by viewModel.imageUrl.observeAsState()
    val audioUrl by viewModel.audioUrl.observeAsState()
    val notes by viewModel.notes.observeAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        TextField(
            value = noteText,
            onValueChange = {
                noteText = it
                viewModel.onNoteChange(it.text)
            },
            label = { Text("Wprowadź notatkę") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.captureLocation() }, modifier = Modifier.fillMaxWidth()) {
            Text("Pobierz lokalizację")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.addImage() }, modifier = Modifier.fillMaxWidth()) {
            Text("Dodaj zdjęcie")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.recordAudio() }, modifier = Modifier.fillMaxWidth()) {
            Text("Nagraj dźwięk")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.submitNote() }, modifier = Modifier.fillMaxWidth()) {
            Text("Zatwierdź notatkę")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            context.startActivity(android.content.Intent(context, MapActivity::class.java))
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Pokaż na mapie")
        }

        Spacer(modifier = Modifier.height(16.dp))

        location?.let {
            Text("Lokalizacja: ${'$'}{it.latitude}, ${'$'}{it.longitude}")
        }
        imageUrl?.let {
            Text("Zdjęcie: ${'$'}it")
        }
        audioUrl?.let {
            Text("Audio: ${'$'}it")
        }

        Spacer(modifier = Modifier.height(16.dp))

        notes.forEach { note ->
            Text("- ${'$'}{note.text} (${ '$'}{note.location})")
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Digital Diary",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DigitalDiaryTheme {
        MainScreen(viewModel = MainViewModel(Application()))
    }
}