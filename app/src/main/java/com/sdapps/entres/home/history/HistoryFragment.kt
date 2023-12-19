package com.sdapps.entres.home.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sdapps.entres.R
import com.sdapps.entres.databinding.FragmentHistoryBinding
import com.sdapps.entres.ui.login.loginBO

class HistoryFragment : Fragment() {


    private lateinit var binding: FragmentHistoryBinding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[VM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    fun init(){
        viewModel.data.observe(viewLifecycleOwner){
            dataFromAliens ->
            binding.role.text = dataFromAliens
        }
    }


}