package com.sdapps.entres

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdapps.entres.databinding.ActivityMainBinding

class BaseActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.orderHistory -> {
                    val navController = findNavController(R.id.nav_host_fragment_activity_home_class)
                    navController.navigate(R.id.orderHistory)
                    return@setOnItemSelectedListener true
                }

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

                else -> { false }
            }
        }


    }
}