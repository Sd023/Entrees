package com.sdapps.entres.main.home.ordertaking.orderfrag.presenter

interface OrderTakingManager {

    interface View{
        fun showDialog(position: Int)
        fun setupView(list: ArrayList<Int>,map: HashMap<Int,String>)
    }


    interface Presenter{

        fun attachView(view: View)
        fun loadUserDetails()

        fun tableRef()
    }
}