package com.sdapps.entres.Commons

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.sdapps.entres.R

class CommonDialog(var title: String, var isCancelableDialog: Boolean, appContext: Context): Dialog(appContext) {


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        var view: View? = null
        view = View.inflate(context, R.layout.common_dialog_layout, null)

        setContentView(view)
        setCancelable(isCancelableDialog)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitle = view.findViewById<TextView>(R.id.titleDialog)
        val mDoneBTN = view.findViewById<Button>(R.id.btn_done)

        txtTitle.text = title


    }


}