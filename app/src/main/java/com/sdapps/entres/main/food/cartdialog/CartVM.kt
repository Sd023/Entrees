package com.sdapps.entres.main.food.cartdialog

import android.app.ProgressDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdapps.entres.main.food.main.FoodBO
import kotlinx.coroutines.launch

class CartVM(var repo: CartRepo) : ViewModel() {

    fun insertDataToDB(list: ArrayList<FoodBO>, tableName: String, seat: String){
        viewModelScope.launch {
            repo.insertData(list, tableName, seat)
        }

    }
}