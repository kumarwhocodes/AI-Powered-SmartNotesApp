package dev.kumar.assignment.utils

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dev.kumar.assignment.data.toLocalUser

fun checkAndStoreUser(
    user: FirebaseUser?
) {
    val userRef = FirebaseFirestore.getInstance()
        .collection("users")
        .document(user?.uid.toString())

    userRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val document = task.result
            if (!document.exists()) {
                storeUserData(user)
            } else {
                Log.d("Firestore", "User already stored in firebase.")
            }
        }
    }
}

private fun storeUserData(user: FirebaseUser?) {
    if (user == null) return

    val userRef = FirebaseFirestore.getInstance()
        .collection("users")
        .document(user.uid)

    userRef.set(user.toLocalUser())
        .addOnSuccessListener {
            Log.d("Firestore", "Stored user in firebase.")
        }
        .addOnFailureListener {
            Log.d("Firestore", "Unable to store user in firebase.")
        }
}
