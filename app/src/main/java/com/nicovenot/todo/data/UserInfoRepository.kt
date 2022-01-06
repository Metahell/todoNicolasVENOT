package com.nicovenot.todo.data

import android.content.Context
import android.widget.Toast
import com.nicovenot.todo.model.*
import com.nicovenot.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserInfoRepository {
    private val webService = Api.userWebService

    private fun convert(bytes: ByteArray): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpg",
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

    suspend fun getAccount(u: LoginForm) : LoginResponse? {
        val l = webService.login(u);
        return if (l.isSuccessful) l.body() else null
    }

    suspend fun addAccount(u: SignUpForm,c: Context?) : SignUpResponse? {
        val l = webService.register(u);
        if (l.isSuccessful){
            return l.body()
        }
        else {
            Toast.makeText(c, "La requête n'a pas abouti", Toast.LENGTH_LONG).show()
            return null
        }
    }
}