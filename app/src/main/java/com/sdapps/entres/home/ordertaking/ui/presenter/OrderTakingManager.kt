package com.sdapps.entres.home.ordertaking.ui.presenter

interface OrderTakingManager {

    interface View{
        fun showDialog(position: Int)
        fun setupView(list: ArrayList<Int>,map: HashMap<Int,String>)
    }


    interface Presenter{

        fun attachView(view: OrderTakingManager.View)
        fun loadUserDetails()

        fun tableRef()
    }
}