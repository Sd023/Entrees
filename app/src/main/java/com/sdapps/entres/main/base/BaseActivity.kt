package com.sdapps.entres.main.base

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdapps.entres.R
import com.sdapps.entres.databinding.ActivityMainBinding


open class BaseActivity : AppCompatActivity(), BaseView {


    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.sdapps.entres.R.color.black)
        }
       /* val bundle = intent.extras
        var str : String? =null
        var uid : String? = null
        if(bundle != null){
            str = bundle.getString("role")
            uid = bundle.getString("uid")
        }*/



        val navView: BottomNavigationView = binding.navView

        navView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.orderTaking -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_home_class)
                    navController.navigate(R.id.orderTaking)
                    return@setOnItemSelectedListener true
                }

                R.id.profile -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_home_class)
                    navController.navigate(R.id.profile)
                    return@setOnItemSelectedListener true
                }

                R.id.orderHistory -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_home_class)
                    navController.navigate(R.id.orderHistory)
                    return@setOnItemSelectedListener true
                }

                else -> { false }
            }
        }


    }

    override fun showError(msg: String) {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_LONG).show()
    }

    override fun showAlert(title: String) {
        val layoutInflator = this.layoutInflater
        val dialogView = layoutInflator.inflate(R.layout.common_dialog_layout,null)
        dialog = AlertDialog.Builder(this).setView(dialogView)
        val dialogText = dialogView.findViewById<TextView>(R.id.titleDialog)
        val btn = dialogView.findViewById<Button>(R.id.btn_done)
        dialogText.text = title
        alert = dialog.create()
        alert.show()

        btn.setOnClickListener{
            alert.dismiss()
        }

    }


}