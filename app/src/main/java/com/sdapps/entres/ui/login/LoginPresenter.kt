package com.sdapps.entres.ui.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class LoginPresenter: LoginHelper.Presenter {

    private lateinit var view: LoginHelper.View
    private lateinit var context: Context

    override fun attachView(view: LoginHelper.View, context: Context) {
        this.view = view
        this.context = context
    }

    override fun detachView() {
    }

    override fun login(userName: String, password: String) {

    }

    override suspend fun register(firebaseAuth: FirebaseAuth,userName: String, password: String) {
        try{
            var role: String
            CoroutineScope(Dispatchers.IO).launch {
                if(userName.lowercase().contains("_m")){
                    role = "manager"
                    firebaseAuth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener {
                            if(it.isSuccessful){
                                view.checkAndAuthorizeLogin(role)
                            }else{
                                view.showError(it.exception?.message)
                            }
                        }
                }else if(userName.lowercase().contains("_w")){
                    role = "waiter"
                    firebaseAuth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            view.checkAndAuthorizeLogin(role)
                        }else{
                            view.showError(it.exception?.message)
                        }
                    }
                }else if(userName.lowercase().contains("_c")){
                    role = "chef"
                    firebaseAuth.createUserWithEmailAndPassword(userName,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            view.checkAndAuthorizeLogin(role)
                        }else{
                            view.showError(it.exception?.message)
                        }
                    }
                }
            }


        }catch (ex: Exception){
            ex.message
        }
    }

}