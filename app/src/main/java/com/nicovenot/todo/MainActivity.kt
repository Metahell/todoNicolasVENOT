package com.nicovenot.todo


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nicovenot.todo.network.Api
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences("TOKEN_SHARE", Context.MODE_PRIVATE)
        Api.TOKEN = pref.getString("TOKEN", "null").toString()
        setContentView(R.layout.activity_main)
    }

}