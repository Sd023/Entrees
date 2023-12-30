package com.sdapps.entres.main.home.ordertaking.orderfrag.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.databinding.FragmentOrderTakingBinding
import com.sdapps.entres.main.home.ordertaking.orderfrag.presenter.OrderTakingManager
import com.sdapps.entres.main.home.ordertaking.orderfrag.presenter.OrderTakingPresenter
import com.sdapps.entres.main.home.ordertaking.dialog.CommonDialog

class OrderTakingFragment : Fragment() , OrderTakingManager.View {

    private var binding: FragmentOrderTakingBinding? = null
    private lateinit var context : Context

    private lateinit var presenter: OrderTakingPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context = requireContext().applicationContext
        binding = FragmentOrderTakingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = OrderTakingPresenter(context)
        presenter.attachView(this)
        initializeFirebase()
    }

    fun initializeFirebase(){
        presenter.loadUserDetails()
        presenter.tableRef()
    }


    override fun setupView(list: ArrayList<Int>, map: HashMap<Int, String>) {
        try {
            binding!!.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            val adapter = OrderTakingAdapter(list,map, requireContext(),this)
            binding!!.recyclerView.adapter = adapter
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }



    override fun showDialog(position : Int) {
        val dialog = CommonDialog()
        val args = Bundle()
        args.putInt("POSITION",position)
        dialog.arguments = args
        dialog.show(childFragmentManager, CommonDialog.TAG)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
