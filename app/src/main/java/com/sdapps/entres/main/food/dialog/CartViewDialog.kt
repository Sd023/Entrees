package com.sdapps.entres.main.food.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.sdapps.entres.databinding.FoodCartBinding
import com.sdapps.entres.main.food.view.CountVM

class CartViewDialog (private val vm : CountVM): DialogFragment() {

    companion object {
        const val TAG = "FoodDialog"
    }

    private lateinit var binding: FoodCartBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoodCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAll()
    }

    fun initAll(){
        if(vm.cartList.value != null){
            vm.cartList.observe(viewLifecycleOwner){
                val data = it
                binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                val adapter = CartAdapter(data)
                binding.recyclerView.adapter = adapter

            }
        }
    }
}