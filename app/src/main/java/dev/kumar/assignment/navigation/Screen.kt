package dev.kumar.assignment.navigation

sealed class Screen(val route: String) {
    object AuthScreen : Screen("auth")
    object DashboardScreen : Screen("dashboard")
    object AddNoteScreen : Screen("add_note")
    object EditNoteScreen : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: String) = "edit_note/$noteId"
    }
}