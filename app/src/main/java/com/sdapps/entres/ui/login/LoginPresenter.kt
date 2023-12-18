package com.sdapps.entres.ui.login

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginPresenter: LoginHelper.Presenter {

    private lateinit var view: LoginHelper.View
    private lateinit var context: Context

    override fun attachView(view: LoginHelper.View, context: Context) {
        this.view = view
        this.context = context
    }

    override fun detachView() {
    }

    override fun login(firebaseAuth: FirebaseAuth, userName: String, password: String) {

        try{
            firebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    getUserDetailsFromId(firebaseAuth.currentUser?.uid)
                }else{
                   view.showError(task.exception?.message)
                }
            }
        }catch (ex: Exception){
            Log.d("FIREBASE",ex.printStackTrace().toString())
            view.showError(ex.message)
        }
    }

    fun getUserDetailsFromId(userId: String?){
        if(userId != null){

            val dbRef = FirebaseDatabase.getInstance().getReference("users")
            dbRef.child(userId).addListenerForSingleValueEvent(object  : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val role = snapshot.child("role").getValue(String::class.java)
                        val bo = loginBO().apply {
                            userRole = role!!

                        }
                        view.moveToNextScreen(bo)
                    }else{
                        view.showError("Error getting details from firebase!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    view.showError(error.message)
                    Log.d("FIREBASE",error.details)
                }
            })
        }
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