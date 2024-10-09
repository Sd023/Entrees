package com.sdapps.entres.main.login.customLogin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sdapps.entres.main.login.customLogin.ApiClient
import com.sdapps.entres.main.login.customLogin.model.User
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginVM: ViewModel() {
    private val apiClient =ApiClient()

    private val _userCreationStatus = MutableLiveData<Boolean>()
    val userCreationStatus: LiveData<Boolean> get() = _userCreationStatus

    val userData : LiveData<List<User>> = liveData {
        try {
            val response = apiClient.fetchData()
            Log.d("RES", response.toString())
            emit(response)
        }catch (ex: Exception){
            ex.printStackTrace()
            emit(ArrayList())
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            val response = apiClient.createUser(user)
            _userCreationStatus.value = response.status.isSuccess()
        }
    }
}