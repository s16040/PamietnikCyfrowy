package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.activity.compose.rememberLauncherForActivityResult
import android.app.Application
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

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
@OptIn(ExperimentalPermissionsApi::class)
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val location by viewModel.location.observeAsState()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val imageUrl by viewModel.imageUrl.observeAsState()
    val audioUrl by viewModel.audioUrl.observeAsState()
    val notes by viewModel.notes.observeAsState(emptyList())

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.onImageSelected(uri?.toString())
        }
    val audioPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.onAudioSelected(uri?.toString())
        }

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
        if (locationPermissionState.status.isGranted) {
            Button(onClick = { viewModel.captureLocation() }, modifier = Modifier.fillMaxWidth()) {
                Text("Pobierz lokalizację")
            }
        } else {
            Button(onClick = { locationPermissionState.launchPermissionRequest() }, modifier = Modifier.fillMaxWidth()) {
                Text("Poproś o dostęp do lokalizacji")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { imagePickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("Wybierz zdjęcie")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { audioPickerLauncher.launch("audio/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("Wybierz nagranie")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.submitNote()
                noteText = TextFieldValue("")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
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

        LazyColumn {
            items(notes, key = { it.id }) { note ->
                val lat = note.latitude ?: 0.0
                val lng = note.longitude ?: 0.0
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = android.content.Intent(context, NoteDetailActivity::class.java)
                            intent.putExtra("noteId", note.id)
                            context.startActivity(intent)
                        }
                ) {
                    Text(
                        "${'$'}{note.text} (${ '$'}lat, ${ '$'}lng)",
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { viewModel.deleteNote(note.id) }) {
                        Text("Usuń")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
