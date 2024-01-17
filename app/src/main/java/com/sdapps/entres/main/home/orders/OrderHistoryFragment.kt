package com.sdapps.entres.main.home.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.R
import com.sdapps.entres.databinding.FragmentOrderHistoryBinding
import com.sdapps.entres.databinding.HistoryViewBinding


class OrderHistoryFragment : Fragment() {

    private lateinit var binding : FragmentOrderHistoryBinding
    private lateinit var items : ArrayList<OrderHistoryBO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAll()
    }

    fun initAll(){
        items = arrayListOf()

        val bo = OrderHistoryBO().apply {
            tableName = "1"
            totalPrice = "200"
            totalItems = "10"
            status = "preparing"
        }

        items.add(bo)
        val bo1 = OrderHistoryBO().apply {
            tableName = "2"
            totalPrice = "300"
            totalItems = "20"
            status = "done"
        }
        items.add(bo1)

        val bo2 = OrderHistoryBO().apply {
            tableName = "3"
            totalPrice = "600"
            totalItems = "2"
            status = "waiting for pickup"
        }
        items.add(bo2)
        val bo3 = OrderHistoryBO().apply {
            tableName = "4"
            totalPrice = "1200"
            totalItems = "4"
            status = "completed"
        }
        items.add(bo3)

        val bo4 = OrderHistoryBO().apply {
            tableName = "5"
            totalPrice = "2200"
            totalItems = "4"
            status = "preparing"
        }
        items.add(bo4)

        val bo5 = OrderHistoryBO().apply {
            tableName = "6"
            totalPrice = "3200"
            totalItems = "2"
            status = "completed"
        }
        items.add(bo5)

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        val adapter =  OrderHistoryAdapter(items)
        binding.recyclerView.adapter =adapter

    }

}