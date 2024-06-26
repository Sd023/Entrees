package com.sdapps.entres.main.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.sdapps.entres.core.database.DBHandler

interface LoginHelper {

    interface View{

        fun showLoading()
        fun hideLoading()
        fun checkValid(userName: String, password: String)
        fun checkAndRegisterUser(role: String)
        suspend fun createUserRole(role: String) : Boolean

        fun showErrorDialog(msg : String?)

        fun moveToNextScreen()

    }

    interface Presenter{
        fun attachView(view: View, context: Context, dbHandler: DBHandler)
        fun detachView()
        fun login(
            firebaseAuth: FirebaseAuth,
            userName: String,
            password: String
        )

        suspend fun register(firebaseAuth: FirebaseAuth,userName: String, password: String)



    }
}