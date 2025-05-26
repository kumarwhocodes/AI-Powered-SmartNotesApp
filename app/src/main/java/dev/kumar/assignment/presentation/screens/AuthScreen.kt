package dev.kumar.assignment.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var isLoading by remember { mutableStateOf(false) }
    val token = Constants.WEB_CLIENT_ID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome text
        Text(
            text = "Welcome",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Sign in to continue",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        GoogleSignInButton(
            context = context,
            onAuthComplete = { result ->
                isLoading = false
                user = result.user
                checkAndStoreUser(user)
                navController.navigate(Screen.DashboardScreen.route) {
                    popUpTo(Screen.AuthScreen.route) {
                        inclusive = true
                    }
                }
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            },
            onAuthError = { exception ->
                isLoading = false
                user = null
                val errorMessage = when {
                    exception.message?.contains("user_canceled") == true -> "Sign-in was cancelled"
                    exception.message?.contains("no_credential") == true -> "No Google accounts found"
                    else -> "Login Failed: ${exception.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            },
            token = token
        )
    }
}