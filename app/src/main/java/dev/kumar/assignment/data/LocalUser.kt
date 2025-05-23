package dev.kumar.assignment.data

import com.google.firebase.auth.FirebaseUser

data class LocalUser(
    val name: String?,
    val username: String?,
    val uid: String,
    val email: String,
    val photoUrl: String? = "https://img.freepik.com/premium-vector/young-smiling-man-holding-pointing-blank-screen-laptop-computer-distance-elearning-education-concept-3d-vector-people-character-illustration-cartoon-minimal-style_365941-927.jpg",
    val phoneNumber: String? = null
)

fun FirebaseUser.toLocalUser(): LocalUser {
    return LocalUser(
        name = displayName,
        uid = uid,
        username = displayName,
        email = email ?: "",
        photoUrl = photoUrl?.toString(),
        phoneNumber = phoneNumber
    )
}