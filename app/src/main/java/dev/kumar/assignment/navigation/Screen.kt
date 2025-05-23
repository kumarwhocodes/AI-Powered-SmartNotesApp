package dev.kumar.assignment.navigation

sealed class Screen(val route: String) {

    data object AuthScreen: Screen("auth")
    data object DashboardScreen: Screen("dashboard")
    data object AddNoteScreen: Screen("add_note")

}