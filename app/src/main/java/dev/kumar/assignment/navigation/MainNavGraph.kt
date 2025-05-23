package dev.kumar.assignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.kumar.assignment.presentation.AuthScreen
import dev.kumar.assignment.presentation.DashboardScreen

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {

    fun isUserLoggedIn(): Boolean {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return user != null
    }

    val initialScreen = if (isUserLoggedIn()) Screen.DashboardScreen else Screen.AuthScreen

    NavHost(
        navController = navController,
        startDestination = initialScreen.route
    ) {
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController)
        }
        composable(Screen.DashboardScreen.route) {
            DashboardScreen(navController)
        }
    }
}