package com.sdapps.entres.main.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sdapps.entres.R
import com.sdapps.entres.databinding.FragmentProfileBinding
import com.sdapps.entres.main.food.BaseFoodFragment
import com.sdapps.entres.network.NetworkTools

class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfile()
    }

    fun initProfile(){
        if(NetworkTools().isAvailableConnection(requireContext())){
            viewModel.getUserProfileData().observe(viewLifecycleOwner, Observer {
                it?.let {
                    binding.profileName.text = it.name
                    binding.email.text = it.email
                }

            })
        }else{
            showAlert("Cannot connect to network")
        }


    }


    fun showAlert(err: String){
        val layoutInflator = this.layoutInflater
        val dialogView = layoutInflator.inflate(R.layout.common_dialog_layout,null)
        dialog = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialogText = dialogView.findViewById<TextView>(R.id.titleDialog)
        val btn = dialogView.findViewById<Button>(R.id.btn_done)
        dialogText.text = err
        dialog.setCancelable(false)
        alert = dialog.create()
        alert.show()

        btn.setOnClickListener{
            alert.dismiss()
            initProfile()
        }
    }
}