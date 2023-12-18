package com.sdapps.entres.ui.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

interface LoginHelper {

    interface View{
        fun checkValid(userName: String, password: String)
        fun checkAndAuthorizeLogin(role: String)
        suspend fun createUserRole(role: String) : Boolean

        fun showError(msg : String?)

        fun moveToNextScreen(loginBO: loginBO)

    }

    interface Presenter{
        fun attachView(view: LoginHelper.View, context: Context)
        fun detachView()
        fun login(
            firebaseAuth: FirebaseAuth,
            userName: String,
            password: String
        )

        suspend fun register(firebaseAuth: FirebaseAuth,userName: String, password: String)



    }
}