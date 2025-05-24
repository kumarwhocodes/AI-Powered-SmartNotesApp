package dev.kumar.assignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val userId: String = "",
    val isDeleted: Boolean = false,
    val isSynced: Boolean = false
) {
    // Convert to Firebase format
    fun toFirebaseMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "content" to content,
        "createdAt" to Timestamp(Date(createdAt)),
        "updatedAt" to Timestamp(Date(updatedAt)),
        "userId" to userId,
        "isDeleted" to isDeleted
    )

    companion object {
        // Convert from Firebase format
        fun fromFirebaseMap(data: Map<String, Any>, documentId: String): Note {
            return Note(
                id = documentId,
                title = data["title"] as? String ?: "",
                content = data["content"] as? String ?: "",
                createdAt = (data["createdAt"] as? Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                updatedAt = (data["updatedAt"] as? Timestamp)?.toDate()?.time ?: System.currentTimeMillis(),
                userId = data["userId"] as? String ?: "",
                isDeleted = data["isDeleted"] as? Boolean ?: false,
                isSynced = true
            )
        }
    }
}
