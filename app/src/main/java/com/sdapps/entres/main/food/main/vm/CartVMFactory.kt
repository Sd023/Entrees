package com.sdapps.entres.main.food.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartVMFactory (private val repo : CartRepo) :  ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartViewModel::class.java)){
            return CartViewModel(repo) as T
        }
        throw IllegalAccessException("Unknown VM")
    }
}