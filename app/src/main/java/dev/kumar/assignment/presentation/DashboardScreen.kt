package dev.kumar.assignment.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.kumar.assignment.navigation.Screen

@Composable
fun DashboardScreen(navController: NavController) {

    val username = Firebase.auth.currentUser?.displayName
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hi $username")

        PrimaryButton(
            onClick = {
                Firebase.auth.signOut()
                navController.navigate(Screen.AuthScreen.route)
            },
            modifier = Modifier,
            enabled = true,
            shapes = RoundedCornerShape(size = 16.dp),
            content = {
                Text("Logout")
            }
        )
    }
}