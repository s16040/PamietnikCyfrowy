package com.example.digitaldiary.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.example.digitaldiary.model.Note
import com.example.digitaldiary.data.DiaryDatabase
import com.example.digitaldiary.data.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _note = MutableLiveData<String>()
    val note: LiveData<String> = _note

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: LiveData<String?> get() = _imageUrl

    private val _audioUrl = MutableLiveData<String?>()
    val audioUrl: LiveData<String?> get() = _audioUrl

    private val repository: NoteRepository
    val notes: LiveData<List<Note>>

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    init {
        val dao = DiaryDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        notes = repository.allNotes.asLiveData()
    }

    fun onNoteChange(newNote: String) {
        _note.value = newNote
    }

    fun captureLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        _location.postValue(task.result)
                    } else {
                        // Obsługa błędu pobierania lokalizacji
                    }
                })
            } catch (e: SecurityException) {
                // Obsługa wyjątku
            }
        }
    }

    fun onImageSelected(uri: String?) {
        _imageUrl.value = uri
    }

    fun onAudioSelected(uri: String?) {
        _audioUrl.value = uri
    }

    fun submitNote() {
        val text = _note.value ?: return
        val newNote = Note(
            id = System.currentTimeMillis().toString(),
            text = text,
            latitude = _location.value?.latitude,
            longitude = _location.value?.longitude,
            imageUrl = _imageUrl.value,
            audioUrl = _audioUrl.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(newNote)
        }

        // Wyczyszczenie pól po zapisaniu notatki
        _note.value = ""
        _imageUrl.value = null
        _audioUrl.value = null
    }

    fun deleteNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }
}
