package com.sdapps.entres

interface BaseEntreesManager {

    interface BaseEntreesView{

        fun showLoading()
        fun hideLoading()

    }

    interface BaseEntreesPresenter{


        fun downloadUserDetails()
    }

}