package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import com.example.digitaldiary.ui.theme.DigitalDiaryTheme
import com.example.digitaldiary.viewmodel.MainViewModel
import com.example.digitaldiary.viewmodel.MainViewModelFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import androidx.compose.runtime.LaunchedEffect

class MapActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notes by viewModel.notes.observeAsState(emptyList())
            DigitalDiaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(notes)
                }
            }
        }
    }
}

@Composable
fun MapScreen(notes: List<com.example.digitaldiary.model.Note>) {
    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current

    LaunchedEffect(notes) {
        if (notes.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            notes.forEach { note ->
                val lat = note.latitude
                val lng = note.longitude
                if (lat != null && lng != null) {
                    boundsBuilder.include(LatLng(lat, lng))
                }
            }
            val bounds = boundsBuilder.build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        notes.forEach { note ->
            val lat = note.latitude
            val lng = note.longitude
            if (lat != null && lng != null) {
                Marker(
                    position = LatLng(lat, lng),
                    title = note.text,
                    onClick = {
                        val intent = android.content.Intent(context, NoteDetailActivity::class.java)
                        intent.putExtra("noteId", note.id)
                        context.startActivity(intent)
                        true
                    }
                )
            }
        }
    }
}
