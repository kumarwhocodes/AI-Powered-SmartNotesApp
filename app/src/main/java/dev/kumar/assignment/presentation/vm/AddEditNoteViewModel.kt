package dev.kumar.assignment.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kumar.assignment.data.Note
import dev.kumar.assignment.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    private val _noteText = MutableStateFlow("")
    val noteText: StateFlow<String> = _noteText.asStateFlow()

    private val _noteTitle = MutableStateFlow("")
    val noteTitle: StateFlow<String> = _noteTitle.asStateFlow()

    private var currentNoteId: String? = null
    private var originalNote: Note? = null // Store the original note

    fun loadNote(noteId: String?) {
        if (noteId != null) {
            currentNoteId = noteId
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    originalNote = note // Store the original note
                    _noteTitle.value = note.title
                    _noteText.value = note.content
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _noteTitle.value = title
    }

    fun updateText(text: String) {
        _noteText.value = text
    }

    fun saveNote(onSaved: () -> Unit) {
        viewModelScope.launch {
            val title = _noteTitle.value.trim()
            val content = _noteText.value.trim()

            if (title.isEmpty() && content.isEmpty()) return@launch

            if (currentNoteId != null && originalNote != null) {
                // Update existing note - preserve all original fields
                val updatedNote = originalNote!!.copy(
                    title = title,
                    content = content
                    // updatedAt and isSynced will be set in repository.updateNote()
                )
                repository.updateNote(updatedNote)
            } else {
                // Create new note
                val note = Note(
                    id = "", // Will be generated in repository
                    title = title,
                    content = content
                )
                repository.insertNote(note)
            }

            onSaved()
        }
    }
}