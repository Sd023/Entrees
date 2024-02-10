package com.sdapps.entres

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sdapps.entres.databinding.ActivityPrintPreviewBinding
import java.io.File

class PrintPreviewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPrintPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPrintPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filePath = intent.getStringExtra("file_path")
        if (filePath!!.isNotEmpty()) {
            val file = File(filePath)
            if (file.exists()) {
                binding.textViewPreview.text = file.readText()
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.printBtn.setOnClickListener {

        }

    }
}