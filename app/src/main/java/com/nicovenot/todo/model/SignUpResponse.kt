package com.nicovenot.todo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("token")
    val token: String,
    @SerialName("expire")
    val expire: String
)