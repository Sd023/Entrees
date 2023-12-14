package com.sdapps.entres.home.ordertaking

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.BaseActivity
import com.sdapps.entres.databinding.FragmentOrderTakingBinding
import com.sdapps.entres.view.CommonDialog

class OrderTakingFragment : Fragment() , OrderTaking.View{

    private var binding: FragmentOrderTakingBinding? = null
    private lateinit var context : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderTakingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }
    fun setupView() {
        try {
            val data = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
            binding!!.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            val adapter = OrderTakingAdapter(data, requireContext(),this)
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
        dialog.show(childFragmentManager,CommonDialog.TAG)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
