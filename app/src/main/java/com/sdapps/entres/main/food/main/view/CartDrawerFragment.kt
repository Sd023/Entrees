package com.sdapps.entres.main.food.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.databinding.DrawerContentMainBinding
import com.sdapps.entres.main.food.main.vm.CartRepo
import com.sdapps.entres.main.food.main.vm.CartVMFactory
import com.sdapps.entres.main.food.main.vm.CartViewModel

class CartDrawerFragment : Fragment() {

    private lateinit var binding: DrawerContentMainBinding
    private lateinit var vm: CartViewModel

    private var tableName: String? = null
    private var seat: String? = null

    private lateinit var repo : CartRepo

    private val viewModel by lazy {
        repo = CartRepo(requireContext())
        ViewModelProvider(this, CartVMFactory(repo))[CartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DrawerContentMainBinding.inflate(layoutInflater, container, false)
        vm = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.cartList.observe(viewLifecycleOwner) { itemList ->
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            val adapter = CartDrawerAdapter(requireContext(), itemList)
            binding.recyclerView.adapter = adapter


            binding.cartOrderBtn.setOnClickListener {

                viewModel.insertDataToDB(itemList,"","")
                viewModel.resetCount()
                itemList.clear()
                adapter.notifyDataSetChanged()
            }
        }


    }


}