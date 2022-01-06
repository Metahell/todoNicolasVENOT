package com.nicovenot.todo.network

import android.content.Context
import android.preference.PreferenceManager
import com.nicovenot.todo.service.TasksWebService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nicovenot.todo.authentication.SHARED_PREF_TOKEN_KEY
import com.nicovenot.todo.service.UserWebService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences

object Api {
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/v1/"
    public var TOKEN = ""
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", TOKEN)
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    val userWebService: UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}