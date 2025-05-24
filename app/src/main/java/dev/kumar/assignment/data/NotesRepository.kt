package dev.kumar.assignment.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private var firestoreListener: ListenerRegistration? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getAllNotes(): Flow<List<Note>>? {
        val userId = getCurrentUserId() ?: return null
        return noteDao.getAllNotes(userId)
    }

    suspend fun getNoteById(id: String): Note? = noteDao.getNoteById(id)

    suspend fun insertNote(note: Note) {
        val userId = getCurrentUserId() ?: return
        val noteWithUser = note.copy(
            id = if (note.id.isEmpty()) UUID.randomUUID().toString() else note.id,
            userId = userId,
            isSynced = false
        )
        noteDao.insertNote(noteWithUser)
        coroutineScope.launch {
            syncToFirestore(noteWithUser)
        }
    }

    suspend fun updateNote(note: Note) {
        val updatedNote = note.copy(
            updatedAt = System.currentTimeMillis(),
            isSynced = false
        )
        noteDao.updateNote(updatedNote)
        coroutineScope.launch {
            syncToFirestore(updatedNote)
        }
    }

    suspend fun deleteNote(id: String) {
        noteDao.softDeleteNote(id)
        val note = noteDao.getNoteById(id)
        if (note != null) {
            val deletedNote = note.copy(isDeleted = true, isSynced = false)
            // Sync deletion to Firestore in background (non-blocking)
            coroutineScope.launch {
                syncToFirestore(deletedNote)
            }
        }
    }

    private suspend fun syncToFirestore(note: Note) {
        try {
            firestore.collection("notes")
                .document(note.id)
                .set(note.toFirebaseMap())
                .await()
            noteDao.markAsSynced(note.id)
            Log.d("NotesRepository", "Note synced to Firestore: ${note.id}")
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to sync note to Firestore", e)
        }
    }

    suspend fun syncUnsyncedNotes() {
        val userId = getCurrentUserId() ?: return
        try {
            val unsyncedNotes = noteDao.getUnsyncedNotes(userId)
            unsyncedNotes.forEach { note ->
                syncToFirestore(note)
            }
        } catch (e: Exception) {
            Log.e("NotesRepository", "Failed to sync unsynced notes", e)
        }
    }

    fun startRealtimeSync() {
        val userId = getCurrentUserId() ?: return
        stopRealtimeSync()

        firestoreListener = firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("NotesRepository", "Listen failed", e)
                    return@addSnapshotListener
                }

                snapshot?.let { querySnapshot ->
                    coroutineScope.launch {
                        val firestoreNotes = querySnapshot.documents.mapNotNull { doc ->
                            try {
                                Note.fromFirebaseMap(doc.data ?: emptyMap(), doc.id)
                            } catch (ex: Exception) {
                                Log.e("NotesRepository", "Failed to parse note", ex)
                                null
                            }
                        }

                        // Insert/update notes from Firestore
                        firestoreNotes.forEach { firestoreNote ->
                            val localNote = noteDao.getNoteById(firestoreNote.id)
                            if (localNote == null || localNote.updatedAt < firestoreNote.updatedAt) {
                                noteDao.insertNote(firestoreNote)
                            }
                        }
                    }
                }
            }
    }

    fun stopRealtimeSync() {
        firestoreListener?.remove()
        firestoreListener = null
    }
}