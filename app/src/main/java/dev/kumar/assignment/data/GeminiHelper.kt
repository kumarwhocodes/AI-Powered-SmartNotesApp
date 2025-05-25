package dev.kumar.assignment.data

import com.google.ai.client.generativeai.GenerativeModel

class GeminiHelper(apiKey: String) {

    private val generativeModel = GenerativeModel(
        modelName = "models/gemini-2.0-flash-exp",
        apiKey = apiKey
    )

    suspend fun summarizeNote(noteText: String): String {
        return try {
            val response =
                generativeModel.generateContent("$noteText\n\nSummarize the above note in 3-4 lines.")
            response.text ?: "No response received."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun suggestTitle(noteText: String?, noteTitle: String?): String {
        return try {
            val response =
                generativeModel.generateContent("Note Title: $noteTitle\n Note Text: $noteText\nSuggest me a title for my note in one to two words only. Don't give response like-here are few options, etc...")
            response.text ?: "No response received."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
