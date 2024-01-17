package com.sdapps.entres.main.food.cartdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CartVMFactory(private val repo : CartRepo) :  ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartVM::class.java)){
            return CartVM(repo) as T
        }
        throw IllegalAccessException("Unknown VM")
    }
}