package com.sdapps.entres.main.home.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileVM::class.java)){
            return ProfileVM(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}