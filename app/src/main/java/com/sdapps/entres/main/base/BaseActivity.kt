package com.sdapps.entres.main.base

import android.content.Intent
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
import com.sdapps.entres.R
import com.sdapps.entres.databinding.ActivityMainBinding
import com.sdapps.entres.main.homenew.HomeHostActivityNew


open class BaseActivity : AppCompatActivity(), BaseView {


    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog

    companion object {
        val PROFILE = "profile"
        val ORDER_HISTORY = "ord_history"
    }



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
        binding.profileView.setOnClickListener {
            switchToFragment(PROFILE)
        }

        binding.orderHistory.setOnClickListener {
            switchToFragment(ORDER_HISTORY)
        }

    }

    fun switchToFragment(tag: String){
        when(tag) {
            PROFILE -> {
                val intent = Intent(this@BaseActivity, HomeHostActivityNew::class.java)
                intent.putExtra("frag_name", PROFILE)
                startActivity(intent)
            }

            ORDER_HISTORY -> {
                val intent =Intent(this@BaseActivity, HomeHostActivityNew::class.java)
                intent.putExtra("frag_name", ORDER_HISTORY)
                startActivity(intent)
            }
            else -> showError("Unable to load data")
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