package dev.kumar.assignment.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dev.kumar.assignment.presentation.screens.AddEditNoteScreen
import dev.kumar.assignment.presentation.screens.AuthScreen
import dev.kumar.assignment.presentation.screens.DashboardScreen
import dev.kumar.assignment.presentation.vm.AddEditNoteViewModel
import dev.kumar.assignment.presentation.vm.DashboardViewModel

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {

    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination =
        if (isUserLoggedIn) Screen.DashboardScreen.route else Screen.AuthScreen.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController)
        }

        composable(Screen.DashboardScreen.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onAddNoteClick = {
                    navController.navigate(Screen.AddNoteScreen.route)
                },
                onNoteClick = { noteId ->
                    navController.navigate(Screen.EditNoteScreen.createRoute(noteId))
                },
                onSignOut = {
                    navController.navigate(Screen.AuthScreen.route) {
                        popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddNoteScreen.route) {
            val viewModel: AddEditNoteViewModel = hiltViewModel()
            AddEditNoteScreen(
                viewModel = viewModel,
                onSaveClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.EditNoteScreen.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            val viewModel: AddEditNoteViewModel = hiltViewModel()

            viewModel.loadNote(noteId)

            AddEditNoteScreen(
                viewModel = viewModel,
                onSaveClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}