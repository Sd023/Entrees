package com.sdapps.entres.main.home.ordertaking.dialog

import com.sdapps.entres.main.login.data.HotelBO

interface CommonDialogView {

    interface View{
        fun switchActivity(position: Int,seat: String)

        fun setupView(map: HashMap<String, ArrayList<HotelBO.Seats>>?)
    }
}