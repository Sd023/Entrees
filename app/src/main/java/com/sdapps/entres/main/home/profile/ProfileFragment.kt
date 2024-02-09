package com.sdapps.entres.main.home.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sdapps.entres.R
import com.sdapps.entres.core.commons.ClickGuard
import com.sdapps.entres.databinding.FragmentProfileBinding
import com.sdapps.entres.main.BaseEntreesFragment
import com.sdapps.entres.main.food.BaseFoodFragment
import com.sdapps.entres.network.NetworkTools

class ProfileFragment() : BaseEntreesFragment(){


    private lateinit var binding: FragmentProfileBinding
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog

    private lateinit var storageRef: FirebaseStorage

    private val viewModel by lazy {
        ViewModelProvider(this, ProfileFactory(requireActivity().application))[ProfileVM::class.java]
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
                it?.let { attr ->
                    binding.profileName.text = attr.name
                    binding.email.text= attr.email
                    binding.profileRole.text = attr.role
                    binding.dayOrders.text = attr.dayOrder.toString() ?: ""
                    binding.totalOrders.text = attr.totalOrders.toString() ?: ""
                    binding.phone.text = attr.contact
                    binding.rank.text = attr.rank.toString() ?: ""
                    binding.hotelBranch.text = attr.hotelBranch.toString()
                    binding.hotelName.text = attr.hotelName.toString()
                    val ref= Firebase.storage.getReferenceFromUrl(attr.imgUrl!!)

                    ref.downloadUrl.addOnCompleteListener(OnCompleteListener{ task : Task<Uri> ->

                        if(task.isSuccessful){
                            val img = task.result.toString()

                            Glide.with(this@ProfileFragment)
                                .load(img)
                                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.profileImg)
                        }else{
                            Glide.with(this@ProfileFragment).load(R.drawable.ic_launcher_foreground).into(binding.profileImg)
                        }


                    })


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