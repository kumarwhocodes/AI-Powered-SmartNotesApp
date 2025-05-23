package dev.kumar.assignment.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.kumar.assignment.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun GoogleSignInButton(
    context: Context,
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit,
    token: String
) {
    val googleSignInClient = rememberGoogleSignInClient(context, token)
    val launcher = rememberFirebaseAuthLauncher(onAuthComplete, onAuthError)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
            .border(
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                googleSignInClient.signOut().addOnCompleteListener {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "Google Logo",
            modifier = Modifier
                .size(32.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Continue with Google",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            letterSpacing = 0.15.sp
        )
    }
}

@Composable
fun rememberGoogleSignInClient(context: Context, token: String): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                Log.d("GoogleAuth", "Google account retrieved: ${account.email}")

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                scope.launch {
                    try {
                        val authResult =
                            FirebaseAuth.getInstance().signInWithCredential(credential).await()
                        onAuthComplete(authResult)

                        // Fetch the ID Token for current user
                        val idToken = FirebaseAuth.getInstance().currentUser?.getIdToken(false)
                            ?.await()?.token
                        Log.d("GoogleAuth", "Firebase ID Token: $idToken")
                    } catch (e: Exception) {
                        Log.e("GoogleAuth", "Firebase sign-in failed: ${e.message}", e)
                        onAuthError(e)
                    }
                }
            } else {
                Log.e("GoogleAuth", "Google sign-in account is null")
                onAuthError(IllegalStateException("Google account is null"))
            }
        } catch (e: ApiException) {
            Log.e("GoogleAuth", "Google sign-in failed: ${e.statusCode}", e)
            onAuthError(e)
        }
    }
}