package com.sdapps.entres.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager.UserOperationException
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.sdapps.entres.BaseActivity
import com.sdapps.entres.databinding.ActivityLoginScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LoginScreen : AppCompatActivity(), LoginHelper.View {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var presenter: LoginPresenter

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter()
        presenter.attachView(this, applicationContext)

        firebaseAuth = Firebase.auth

        val progressBar = binding.loading
        val loginBtn = binding.login



        loginBtn.setOnClickListener {
            progressBar.progress = 1
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            checkValid(username, password)
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
            CoroutineScope(Dispatchers.IO).launch {
                //Only Login Functionality.
                //presenter.register(firebaseAuth, userName, password)
            }

        }

    }

    override fun checkAndAuthorizeLogin(role: String) {
        CoroutineScope(Dispatchers.Main).launch {
            if (createUserRole(role)) {
                moveToNextScreen(role)
            } else {
                showError("Failed to create user!")
            }
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
                    ).child("role")
                    suspendCancellableCoroutine { continuation ->
                        databaseReference.setValue(role)
                            .addOnSuccessListener {
                                continuation.resume(true)
                            }
                            .addOnFailureListener { exception ->
                                showError(exception.message)
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

    override fun showError(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
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
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            Toast.makeText(applicationContext, "Session Expired", Toast.LENGTH_LONG).show()
        }
    }


    fun moveToNextScreen(role: String){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val intent = Intent(this@LoginScreen, BaseActivity::class.java)
        intent.putExtra("uid",userId)
        intent.putExtra("role",role)
        startActivity(intent)
        finish()
    }
}
