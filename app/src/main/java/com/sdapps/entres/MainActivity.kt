package com.sdapps.entres

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.sdapps.entres.Base.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var button : Button
    private lateinit var textV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        textV = findViewById(R.id.textView)

        button.setOnClickListener{
            fetchDataFromServer()
        }

    }

    private fun fetchDataFromServer(){
        CoroutineScope(Dispatchers.Main).launch {
            try{
                val response : List<BO> = apiService.getRes()
                for(urlList in response){
                    Log.d("RESPONSE", ":-> ${urlList.code}")
                    Log.d("RESPONSE", ":-> ${urlList.api}")
                }
                val str: List<Bo1> = apiService.getResponse()
                val msgg = str[0]
                val ms = msgg.msg

                textV.text = ms


            }catch (ex: Exception){
                ex.message
            }

        }
    }
}