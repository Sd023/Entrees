package com.sdapps.entres.main.food.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.databinding.DrawerContentMainBinding
import com.sdapps.entres.main.food.main.presenter.FoodActivityManager
import com.sdapps.entres.main.food.main.vm.CartRepo
import com.sdapps.entres.main.food.main.vm.CartVMFactory
import com.sdapps.entres.main.food.main.vm.CartViewModel

class CartDrawerFragment : Fragment() {

    private lateinit var binding: DrawerContentMainBinding
    private lateinit var vm: CartViewModel

    private var closeListener: FoodActivityManager.View? = null

    private var tableName: String? = null
    private var seat: String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DrawerContentMainBinding.inflate(layoutInflater, container, false)
        vm = ViewModelProvider(requireActivity())[CartViewModel::class.java]

        arguments?.let {
            tableName = it.getString(TABLE_NAME)
            seat = it.getString(SEAT)
        }
        return binding.root
    }


    companion object {
        private const val TABLE_NAME = "table_name"
        private const val SEAT = "seat_name"

        fun newInstance(tableName : String, seat : String): CartDrawerFragment{
            return CartDrawerFragment().apply {
                arguments = Bundle().apply {
                    putString(TABLE_NAME,tableName)
                    putString(SEAT,seat)
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.cartList.observe(viewLifecycleOwner) { itemList ->
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            val adapter = CartDrawerAdapter(requireContext(), itemList)
            binding.recyclerView.adapter = adapter


            binding.cartOrderBtn.setOnClickListener {
                if(tableName!!.isNotEmpty() && seat!!.isNotEmpty()){
                    vm.insertDataToDB(itemList,tableName!!,seat!!)
                    itemList.clear()
                    adapter.notifyDataSetChanged()
                    vm.resetCount()
                    closeListener!!.closeDrawer()
                }else{
                   Toast.makeText(context,"Unable to save Data", Toast.LENGTH_LONG).show()

                }


            }
        }


    }


    public fun closeDrawer(listener: FoodActivityManager.View){
        this.closeListener = listener
    }

}