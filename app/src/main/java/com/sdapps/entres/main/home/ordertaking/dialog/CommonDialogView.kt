package com.sdapps.entres.main.home.ordertaking.dialog

interface CommonDialogView {

    interface View{
        fun switchActivity(position: Int,seat: String)
    }
}