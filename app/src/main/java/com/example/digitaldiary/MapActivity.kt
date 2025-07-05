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
import androidx.compose.ui.Modifier
import com.example.digitaldiary.ui.theme.DigitalDiaryTheme
import com.example.digitaldiary.viewmodel.MainViewModel
import com.example.digitaldiary.viewmodel.MainViewModelFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng

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
    val firstLocation = notes.firstOrNull()?.location?.split(",")
    val defaultLatLng = if (firstLocation != null && firstLocation.size == 2) {
        LatLng(firstLocation[0].toDoubleOrNull() ?: 0.0, firstLocation[1].toDoubleOrNull() ?: 0.0)
    } else {
        LatLng(0.0, 0.0)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(defaultLatLng, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        notes.forEach { note ->
            val parts = note.location.split(",")
            if (parts.size == 2) {
                val lat = parts[0].toDoubleOrNull()
                val lng = parts[1].toDoubleOrNull()
                if (lat != null && lng != null) {
                    Marker(
                        position = LatLng(lat, lng),
                        title = note.text
                    )
                }
            }
        }
    }
}
