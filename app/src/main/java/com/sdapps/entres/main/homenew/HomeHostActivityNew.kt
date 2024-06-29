package com.sdapps.entres.main.homenew

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sdapps.entres.R
import com.sdapps.entres.main.base.BaseActivity.Companion.PROFILE
import com.sdapps.entres.main.base.BaseActivity.Companion.ORDER_HISTORY
import com.sdapps.entres.main.home.orderhistory.OrderHistoryFragment
import com.sdapps.entres.main.home.profile.ProfileFragment

class HomeHostActivityNew : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_host_new)

        if(savedInstanceState == null){
            val fragName = intent.getStringExtra("frag_name")

            val callScreen = when(fragName){
                PROFILE  -> ProfileFragment.initializeProfileView()
                ORDER_HISTORY -> OrderHistoryFragment.initializeOrderHistoryView()
                else -> throw IllegalArgumentException("Unknown Screen")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment,callScreen)
                .commit()
        }



    }
}