package dev.kumar.assignment.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kumar.assignment.data.Note
import dev.kumar.assignment.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        loadNotes()
        repository.startRealtimeSync()
        syncUnsyncedNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            repository.getAllNotes()?.collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    private fun syncUnsyncedNotes() {
        viewModelScope.launch {
            repository.syncUnsyncedNotes()
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun signOut() {
        repository.stopRealtimeSync()
        auth.signOut()
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopRealtimeSync()
    }
}