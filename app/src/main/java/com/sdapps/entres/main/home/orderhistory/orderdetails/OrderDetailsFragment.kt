package com.sdapps.entres.main.home.orderhistory.orderdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.databinding.FragmentOrderDetailsBinding
import com.sdapps.entres.main.home.orderhistory.vm.VM
import java.lang.StringBuilder

class OrderDetailsFragment : Fragment() {
    private var orderId: String? = null
    private lateinit var vm : VM

    private lateinit var binding : FragmentOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(ORDER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        vm = ViewModelProvider(requireParentFragment())[VM::class.java]
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        vm.getOrderDetail(orderId!!)

        binding.ordId.text = orderId
        binding.totalOrderValue.text = StringBuilder().append("Total Value: Rs. ").append(vm.getTotalNetValue()).toString()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        vm.orderDetailList.observe(viewLifecycleOwner){ data ->
            val adapter = OrderDetailsAdapter(data)
            binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            binding.recyclerView.adapter = adapter

        }
    }

    companion object {
        private const val ORDER_ID = "ORDER_ID"
        @JvmStatic
        fun newInstance(param1: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ORDER_ID, param1)
                }
            }
    }
}

