package com.nicovenot.todo.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicovenot.todo.data.UserInfoRepository
import com.nicovenot.todo.model.*
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {
    private val repo = UserInfoRepository();

    suspend fun getInfo(): UserInfo? {
        return repo.getInfo();
    }

    fun updateAvatar(bytes: ByteArray) {
        viewModelScope.launch {
            repo.uploadAvatar(bytes);
        }
    }

    fun updateData(user: UserInfo) {
        viewModelScope.launch {
            repo.updateData(user);
        }
    }

    suspend fun getAccount(u : LoginForm): LoginResponse? {
        return repo.getAccount(u);
    }

    suspend fun addAccount(u: SignUpForm,c: Context?): SignUpResponse? {
        return repo.addAccount(u,c);
    }
}