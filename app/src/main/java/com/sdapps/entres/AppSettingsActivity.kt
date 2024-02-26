package com.sdapps.entres

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sdapps.entres.databinding.ActivityAppSettingsBinding

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppSettingsBinding

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val MODE_KEY = "mode"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private  var isDarkMode : Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingsBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isDarkMode = sharedPreferences.getBoolean(MODE_KEY, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    fun init(){
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.darkSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked || isDarkMode!!){
                saveAndApplyDarkMode(isChecked)
                binding.darkSwitch.text = "Dark"
            }else{
                binding.darkSwitch.text = "Light"
            }


        }

    }

    fun saveAndApplyDarkMode(isTrue: Boolean){
        sharedPreferences.edit().putBoolean(MODE_KEY,isTrue).apply()
    }

}