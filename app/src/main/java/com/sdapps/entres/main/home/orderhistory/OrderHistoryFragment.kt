package com.sdapps.entres.main.home.orderhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.R
import com.sdapps.entres.databinding.FragmentOrderHistoryBinding
import com.sdapps.entres.main.home.orderhistory.orderdetails.OrderDetailsFragment
import com.sdapps.entres.main.home.orderhistory.vm.Listener
import com.sdapps.entres.main.home.orderhistory.vm.OrderHistoryDataManager
import com.sdapps.entres.main.home.orderhistory.vm.OrderHistoryFactory
import com.sdapps.entres.main.home.orderhistory.vm.VM


class OrderHistoryFragment : Fragment(), Listener {

    private lateinit var binding : FragmentOrderHistoryBinding

    private lateinit var repo : OrderHistoryDataManager

    private val viewModel by lazy {
        repo = OrderHistoryDataManager(requireContext())
        ViewModelProvider(this,OrderHistoryFactory(requireActivity().application,repo))[VM::class.java]
    }

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
        viewModel.getPastOrdersFromDB()
        viewModel.orderList.observe(viewLifecycleOwner){
            binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            val adapter =  OrderHistoryAdapter(it)
            adapter.setItemListener(this)
            binding.recyclerView.adapter =adapter
        }
    }

    override fun loadOrderDetail(ordID: String) {
        //viewModel.getOrderDetail(orderId = ordID)
        binding.recyclerView.visibility = View.GONE
        binding.container.visibility= View.VISIBLE

        val detailsFragment = OrderDetailsFragment.newInstance(ordID)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.container,detailsFragment)
        transaction.commit()

    }

}