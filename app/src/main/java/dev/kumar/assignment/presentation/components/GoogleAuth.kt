package dev.kumar.assignment.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
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
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

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
                MaterialTheme.colorScheme.onSurface
            )
            .clickable {
                scope.launch {
                    signInWithGoogle(
                        context = context,
                        credentialManager = credentialManager,
                        webClientId = token,
                        onAuthComplete = onAuthComplete,
                        onAuthError = onAuthError
                    )
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

private suspend fun signInWithGoogle(
    context: Context,
    credentialManager: CredentialManager,
    webClientId: String,
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit
) {
    try {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = context
        )

        val credential = result.credential
        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                Log.d(
                    "GoogleAuth",
                    "User: ${googleIdTokenCredential.displayName} (${googleIdTokenCredential.id})"
                )

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = FirebaseAuth.getInstance()
                    .signInWithCredential(firebaseCredential)
                    .await()

                onAuthComplete(authResult)

                val firebaseIdToken = FirebaseAuth.getInstance().currentUser
                    ?.getIdToken(false)
                    ?.await()?.token
                Log.d("GoogleAuth", "Firebase ID Token: $firebaseIdToken")

            } catch (e: GoogleIdTokenParsingException) {
                onAuthError(e)
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Firebase authentication failed", e)
                onAuthError(e)
            }
        } else {
            val error = IllegalStateException("Unexpected credential type: ${credential.type}")
            Log.e("GoogleAuth", "Unexpected credential type", error)
            onAuthError(error)
        }

    } catch (e: GetCredentialException) {
        Log.e("GoogleAuth", "Credential Manager sign-in failed", e)

        when (e.type) {
            "android.credentials.GetCredentialException.TYPE_USER_CANCELED" -> {
                Log.d("GoogleAuth", "User canceled the sign-in")
            }

            "android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL" -> {
                Log.d("GoogleAuth", "No credentials available")
            }

            else -> {
                Log.e("GoogleAuth", "Unknown credential error: ${e.type}")
            }
        }
        onAuthError(e)
    } catch (e: Exception) {
        Log.e("GoogleAuth", "Unexpected error during sign-in", e)
        onAuthError(e)
    }
}