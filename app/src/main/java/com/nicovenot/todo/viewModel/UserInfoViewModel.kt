package com.nicovenot.todo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicovenot.todo.data.TasksRepository
import com.nicovenot.todo.data.UserInfoRepository
import com.nicovenot.todo.model.Task
import com.nicovenot.todo.model.UserInfo
import kotlinx.coroutines.launch
import com.nicovenot.todo.authentication.LoginForm
import com.nicovenot.todo.authentication.SignUpForm
import com.nicovenot.todo.authentication.AuthenticationResponse
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repo = UserInfoRepository();

    var authenticationResponse: AuthenticationResponse? = null;
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

    fun login(loginDetails: LoginForm) {
        viewModelScope.launch {
            authenticationResponse = repo.login(loginDetails)
        }
    }

    fun signUp(signupDetails: SignUpForm) {
        viewModelScope.launch {
            authenticationResponse = repo.signUp(signupDetails)
        }
    }
}