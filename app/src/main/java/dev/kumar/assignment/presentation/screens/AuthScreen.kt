package dev.kumar.assignment.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.kumar.assignment.navigation.Screen
import dev.kumar.assignment.presentation.components.GoogleSignInButton
import dev.kumar.assignment.utils.Constants
import dev.kumar.assignment.utils.checkAndStoreUser

@Composable
fun AuthScreen(navController: NavController) {

    val context = LocalContext.current
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val token = Constants.WEB_CLIENT_ID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignInButton(
            context = context,
            onAuthComplete = { result ->
                user = result.user
                checkAndStoreUser(user)
                navController.navigate(Screen.DashboardScreen.route) {
                    popUpTo(Screen.AuthScreen.route) {
                        inclusive = true
                    }
                }
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            },
            onAuthError = {
                user = null
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()

            },
            token = token
        )
    }

}