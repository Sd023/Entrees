package com.sdapps.entres.main.food.cartdialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.databinding.FoodCartBinding
import com.sdapps.entres.main.food.main.vm.CartViewModel
import com.sdapps.entres.main.food.main.FoodBO

class CartViewDialog (private val vm : CartViewModel): DialogFragment() {

    companion object {
        const val TAG = "FoodDialog"
    }

    private lateinit var binding: FoodCartBinding

    private lateinit var tableId : String
    private lateinit var seats : String
    private lateinit var tableName : String
    private lateinit var data : ArrayList<FoodBO>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments

         tableId = args!!.getString("tableNumber")!!
         seats = args.getString("SEAT")!!
        tableName = args.getString("TABLENAME")!!

        Log.d("INTENT", "cart dialog $tableId, $seats ,$tableName")
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
                data = it
                binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                val adapter = CartAdapter(data)
                binding.recyclerView.adapter = adapter

            }
        }

        binding.orderBtn.setOnClickListener {
            //insertData(data)

        }
    }


}