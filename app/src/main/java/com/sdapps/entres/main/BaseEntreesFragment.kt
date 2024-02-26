package com.sdapps.entres.main

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.print.PrintHelper
import com.sdapps.entres.R

open class BaseEntreesFragment() : Fragment() {

    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    interface AlertDialogListener{
        fun onClick()
    }

    fun vibrate(){
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if(vibrator.hasVibrator()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect
                        .createOneShot(100,
                            VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // For older devices
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        }
    }

    fun showPrintDialog(listener: AlertDialogListener){
        dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage("Order Created Successfully!")
            .setPositiveButton("PrintOrder"
            ) { dialog, which ->
                listener.onClick()
            }
            .setNegativeButton("Cancel") { dialog, which ->

            }
            .setCancelable(false)
        alert = dialog.create()
        alert.show()

    }



}