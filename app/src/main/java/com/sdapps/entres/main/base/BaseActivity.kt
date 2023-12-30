package com.sdapps.entres.main.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdapps.entres.R
import com.sdapps.entres.core.Commons.CommonDialog
import com.sdapps.entres.databinding.ActivityMainBinding
import com.sdapps.entres.main.home.history.VM

open class BaseActivity : AppCompatActivity(), BaseView {


    private lateinit var binding: ActivityMainBinding


    private val viewModel by lazy {
        ViewModelProvider(this)[VM::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        var str : String? =null
        var uid : String? = null
        if(bundle != null){
            str = bundle.getString("role")
            uid = bundle.getString("uid")
        }



        val navView: BottomNavigationView = binding.navView

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.orderHistory -> {
                    if(str != null){
                        viewModel.setData(str)
                        viewModel.setUid(uid!!)
                    }
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

    override fun showError(msg: String) {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_LONG).show()
    }

    override fun showAlert(title: String) {
        CommonDialog(title,false,this)
    }


}