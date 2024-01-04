package com.sdapps.entres.main.home.ordertaking.orderfrag.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.R
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.FragmentOrderTakingBinding
import com.sdapps.entres.main.home.ordertaking.orderfrag.presenter.OrderTakingManager
import com.sdapps.entres.main.home.ordertaking.orderfrag.presenter.OrderTakingPresenter
import com.sdapps.entres.main.home.ordertaking.dialog.CommonDialog
import com.sdapps.entres.network.NetworkTools

class OrderTakingFragment : Fragment(), OrderTakingManager.View {

    private var binding: FragmentOrderTakingBinding? = null
    private lateinit var context: Context

    private lateinit var presenter: OrderTakingPresenter
    private lateinit var db: DBHandler
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog
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
        db = DBHandler(context)
        presenter.attachView(this, db)
        initializeFirebase()
    }

    fun initializeFirebase() {
        if(NetworkTools().isAvailableConnection(context)){
            presenter.loadUserDetails()
            presenter.tableRef()
        }else{
            showAlertDialog("Cannot connect to Network!")
        }
    }


    override fun setupView(list: ArrayList<Int>, map: HashMap<Int, String>) {
        try {
            binding!!.recyclerView.layoutManager = GridLayoutManager(context, 3)
            val adapter = OrderTakingAdapter(list, map,context, this)
            binding!!.recyclerView.adapter = adapter
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun showAlertDialog(msg: String) {
        val layoutInflator = this.layoutInflater
        val dialogView = layoutInflator.inflate(R.layout.common_dialog_layout,null)
        dialog = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialogText = dialogView.findViewById<TextView>(R.id.titleDialog)
        val btn = dialogView.findViewById<Button>(R.id.btn_done)
        dialogText.text = msg
        dialog.setCancelable(false)
        alert = dialog.create()
        alert.show()

        btn.setOnClickListener{
            alert.dismiss()
            initializeFirebase()
        }
    }


    override fun showDialog(position: Int) {
        val dialog = CommonDialog(presenter)
        val args = Bundle()
        args.putInt("POSITION", position)
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
