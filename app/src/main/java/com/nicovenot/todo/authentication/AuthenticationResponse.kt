package com.nicovenot.todo.authentication
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data class to store API response data upon authentication.
@Serializable
data class AuthenticationResponse(
    @SerialName("token")
    val apiToken: String
)