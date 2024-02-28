package com.sdapps.entres.main.login.view

import android.widget.RelativeLayout.LayoutParams
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.sdapps.entres.R
import com.sdapps.entres.core.commons.ClickGuard
import com.sdapps.entres.main.base.BaseActivity
import com.sdapps.entres.core.date.DateTools
import com.sdapps.entres.core.date.DateTools.Companion.DATE_TIME
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.ActivityLoginNewBinding
import com.sdapps.entres.main.login.LoginHelper
import com.sdapps.entres.main.login.LoginPresenter
import com.sdapps.entres.main.login.data.LoginBO
import com.sdapps.entres.network.NetworkTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LoginScreen : BaseActivity(), LoginHelper.View, View.OnClickListener {

    private lateinit var binding: ActivityLoginNewBinding
    private lateinit var presenter: LoginPresenter

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbHandler : DBHandler
    private lateinit var newBo : LoginBO

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityLifeCycle","onCreate")
        binding = ActivityLoginNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAnimations()
        init()
    }

    fun startAnimations(){
        binding.appNameLogin?.text = getString(R.string.app_name)
        SlideupTransition.slideDownFadeIn(binding.appNameLogin!!,1000)
        startAnimationForCard()
    }

    fun init(){
        dbHandler = DBHandler(this)
        dbHandler.createDataBase()
        newBo = LoginBO()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()

        progressDialog = ProgressDialog(this)
        presenter = LoginPresenter()
        presenter.attachView(this, applicationContext, dbHandler)

        firebaseAuth = Firebase.auth
        val loginBtn = binding.login

        loginBtn.setOnClickListener(this)
        ClickGuard.guard(loginBtn)
    }

    fun startAnimationForCard(){
        SlideupTransition.slideUp(binding.cardLayout!!,1000)
    }

    override fun onClick(v: View?) {
        if(NetworkTools().isAvailableConnection(this@LoginScreen)){
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            checkValid(username, password)
        }else{
            showErrorDialog("No Internet Connection")
        }
    }

    override fun checkValid(userName: String, password: String) {

        if (userName.isEmpty()) {
            binding.username.error = "UserName must not be empty"
        } else if (password.isEmpty()) {
            binding.password.error = "Password must not be empty"
        }


        if (!isUserNameValid(userName)) {
            Toast.makeText(applicationContext, "Invalid Username", Toast.LENGTH_LONG).show()
        } else if (!isPasswordValid(password)) {
            Toast.makeText(applicationContext, "Password length less", Toast.LENGTH_LONG).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                //presenter.register(firebaseAuth, userName, password)
                showLoading()
                presenter.login(firebaseAuth,userName,password)
            }

        }

    }


    override fun showLoading() {
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Checking user session")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.dismiss()
    }
    override fun checkAndRegisterUser(role: String) {
        CoroutineScope(Dispatchers.Main).launch {
            if (createUserRole(role)) {
                //showProgress()
                //moveToNextScreen(role)
            } else {
                showError("Failed to create user!")
            }
        }
    }

    fun showProgress(){
        CoroutineScope(Dispatchers.Main).launch {
            val progressDialog = ProgressDialog(applicationContext)
            progressDialog.setTitle("Success")
            progressDialog.setMessage("Account Created Successfullly, Please Login")
            progressDialog.show()
        }

    }

    override suspend fun createUserRole(role: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid
                if (userId != null) {
                    val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(
                        userId
                    )
                    val map = HashMap<String,String>()
                    map["isFrom"]= "MOBILE"
                    map["createdDate"] = DateTools().now(DATE_TIME)
                    suspendCancellableCoroutine { continuation ->
                        databaseReference.setValue(map)
                            .addOnSuccessListener {
                                continuation.resume(true)
                            }
                            .addOnFailureListener { exception ->
                                showErrorDialog(exception.message)
                                continuation.resume(false)
                            }
                    }
                } else {
                    showError("Session Expired!")
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun showErrorDialog(msg: String?) {
        showAlert(msg!!)
    }

    override fun moveToNextScreen() {
            val intent = Intent(this@LoginScreen, BaseActivity::class.java)
            hideLoading()
            startActivity(intent)
            finish()
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifeCycle","onStart")
        val currentUser = firebaseAuth.currentUser?.uid
        if (currentUser == null) {
           Log.d("USERCURRENT: ", currentUser.toString())
        }else{
            showLoading()
            presenter.getUserDetailsFromId(currentUser,false)
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifeCycle","onPause")
    }

}
