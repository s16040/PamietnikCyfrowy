package com.example.digitaldiary.data

import com.example.digitaldiary.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(id: String) {
        noteDao.deleteById(id)
    }
}
