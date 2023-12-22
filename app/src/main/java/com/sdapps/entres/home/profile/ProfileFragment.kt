package com.sdapps.entres.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sdapps.entres.R
import com.sdapps.entres.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding

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

        init()
    }

    fun init(){

        viewModel.getUserProfileData().observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.userName.text = it.name
                binding.userEmail.text = it.email
            }

        })
    }
}