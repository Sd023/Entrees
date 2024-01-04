package com.sdapps.entres.main.home.ordertaking.orderfrag.presenter

import com.sdapps.entres.core.database.DBHandler

interface OrderTakingManager {

    interface View {
        fun showDialog(position: Int)
        fun setupView(list: ArrayList<Int>, map: HashMap<Int, String>)

        fun showAlertDialog(msg: String)
    }


    interface Presenter {

        fun attachView(view: View, db: DBHandler)
        fun loadUserDetails()

        fun tableRef()
    }
}