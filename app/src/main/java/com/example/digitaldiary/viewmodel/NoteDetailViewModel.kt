package com.example.digitaldiary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.digitaldiary.data.DiaryDatabase
import com.example.digitaldiary.data.NoteRepository
import com.example.digitaldiary.model.Note

class NoteDetailViewModel(application: Application, noteId: String) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val note: LiveData<Note?>

    init {
        val dao = DiaryDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        note = repository.getNoteById(noteId).asLiveData()
    }
}

class NoteDetailViewModelFactory(
    private val application: Application,
    private val noteId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewModel(application, noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
