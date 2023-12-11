package com.sdapps.entres.home.ordertaking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.BaseActivity
import com.sdapps.entres.databinding.FragmentOrderTakingBinding


class OrderTakingFragment : Fragment() {

    private lateinit var binding: FragmentOrderTakingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderTakingBinding.inflate(inflater,container,false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setupView(){
        try{
            val data = arrayListOf(1,2,3,4,5,6,7,8,9)
            binding.recyclerView.layoutManager  = GridLayoutManager(requireContext(),2)
            val adapter = OrderTakingAdapter(data,requireContext())
            binding.recyclerView.adapter = adapter
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }


}