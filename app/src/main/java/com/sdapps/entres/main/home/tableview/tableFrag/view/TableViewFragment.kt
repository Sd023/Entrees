package com.sdapps.entres.main.home.tableview.tableFrag.view

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.R
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.FragmentOrderTakingBinding
import com.sdapps.entres.main.home.tableview.tableFrag.presenter.TableViewManager
import com.sdapps.entres.main.home.tableview.tableFrag.presenter.TableViewPresenter
import com.sdapps.entres.main.home.tableview.dialog.CommonDialog
import com.sdapps.entres.network.NetworkTools

class TableViewFragment : Fragment(), TableViewManager.View {

    private var binding: FragmentOrderTakingBinding? = null
    private lateinit var context: Context

    private lateinit var presenter: TableViewPresenter
    private lateinit var db: DBHandler
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var alert: AlertDialog

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context = requireContext().applicationContext
        binding = FragmentOrderTakingBinding.inflate(inflater, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        presenter = TableViewPresenter(context)
        db = DBHandler(context)
        presenter.attachView(this, db)
        initializeFirebase()
    }

    fun initializeFirebase() {
        if(NetworkTools().isAvailableConnection(context)){
            showLoading()
            presenter.loadUserDetails()
            presenter.tableRef()
        }else{
            showAlertDialog("Cannot connect to Network!")
        }
    }


    override fun setupView(list: ArrayList<Int>, map: HashMap<Int, String>) {
        try {
            hideLoading()
            binding!!.recyclerView.layoutManager = GridLayoutManager(context, 3)
            val adapter = TableViewAdapter(list, map,context, this)
            binding!!.recyclerView.adapter = adapter
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun showLoading() {
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Fetching Tables...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.dismiss()
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


    override fun showDialog(position: Int, tableName: String) {
        val dialog = CommonDialog(presenter)
        val args = Bundle()
        args.putInt("POSITION", position)
        args.putString("TABLENAME",tableName)
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
