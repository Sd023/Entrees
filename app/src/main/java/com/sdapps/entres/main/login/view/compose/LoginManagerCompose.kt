package com.sdapps.entres.main.login.view.compose

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.sdapps.entres.core.database.DBHandler

interface LoginManagerCompose {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(errorMsg: String)
        fun navigateToHome()
    }
    interface  Presenter {
        fun attachView(view: LoginManagerCompose.View, context: Context, dbHandler: DBHandler)
        fun login(auth: FirebaseAuth,email: String, password: String)
        fun onDestroy()

    }
}