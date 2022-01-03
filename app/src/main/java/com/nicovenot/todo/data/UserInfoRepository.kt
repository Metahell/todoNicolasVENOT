package com.nicovenot.todo.data

import android.net.Uri
import com.nicovenot.todo.model.Task
import com.nicovenot.todo.model.UserInfo
import com.nicovenot.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.nicovenot.todo.authentication.LoginForm
import com.nicovenot.todo.authentication.SignUpForm
import com.nicovenot.todo.authentication.AuthenticationResponse

class UserInfoRepository {
    private val webService = Api.userWebService

    private fun convert(bytes: ByteArray): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = bytes.toRequestBody()
        )
    }

    suspend fun uploadAvatar(bytes:ByteArray){
        webService.updateAvatar(convert(bytes))
    }

    suspend fun getInfo() : UserInfo? {
        val response = webService.getInfo();
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateData(u: UserInfo) {
        webService.update(u);
    }
    suspend fun login(loginDetails: LoginForm): AuthenticationResponse? {
        var response = webService.login(loginDetails);
        if(response.isSuccessful) {
            return response.body();
        }
        return null;
    }

    suspend fun signUp(signupDetails: SignUpForm): AuthenticationResponse? {
        var response = webService.signUp(signupDetails);
        if(response.isSuccessful) {
            return response.body();
        }
        return null;
    }
}