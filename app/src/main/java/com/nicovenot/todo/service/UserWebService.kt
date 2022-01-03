package com.nicovenot.todo.service

import com.nicovenot.todo.model.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import com.nicovenot.todo.authentication.LoginForm
import com.nicovenot.todo.authentication.SignUpForm
import com.nicovenot.todo.authentication.AuthenticationResponse

interface UserWebService {
    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>;

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body loginDetails: LoginForm): Response<AuthenticationResponse>

    @POST("users/sign_up")
    suspend fun signUp(@Body signupDetails: SignUpForm): Response<AuthenticationResponse>
}