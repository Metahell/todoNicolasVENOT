package com.nicovenot.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicovenot.todo.data.UserInfoRepository
import com.nicovenot.todo.model.*
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository();

    suspend fun getInfo(): UserInfo? {
        return repository.getInfo();
    }

    fun updateAvatar(bytes: ByteArray) {
        viewModelScope.launch {
            repository.uploadAvatar(bytes);
        }
    }

    fun updateData(user: UserInfo) {
        viewModelScope.launch {
            repository.updateData(user);
        }
    }

    suspend fun getAccount(u : LoginForm): LoginResponse? {
        return repository.getAccount(u);
    }

    suspend fun addAccount(u: RegisterForm): RegisterResponse? {
        return repository.addAccount(u);
    }
}