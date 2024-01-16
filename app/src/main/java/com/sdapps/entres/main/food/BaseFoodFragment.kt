package com.sdapps.entres.main.food

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nex3z.notificationbadge.NotificationBadge
import com.sdapps.entres.R
import com.sdapps.entres.core.commons.ClickGuard
import com.sdapps.entres.main.food.view.CountVM
import com.sdapps.entres.main.food.view.FoodBO
import com.sdapps.entres.main.food.view.presenter.FoodActivityManager
import com.sdapps.entres.network.NetworkTools


class BaseFoodFragment : Fragment(), FoodActivityManager.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    private lateinit var dataToShow: List<FoodBO>
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var alert: AlertDialog

    private lateinit var finalList: ArrayList<FoodBO>

    private lateinit var vm: CountVM

    private lateinit var badCount: NotificationBadge
    var count = 1
    private lateinit var progressDialog: ProgressDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            val allData: List<FoodBO> = it.getParcelableArrayList(ARG_ALL_DATA) ?: emptyList()
            val category: String = it.getString(ARG_CATEGORY, "")

            dataToShow = filterDataByCategory(allData, category)
        }

        val view = inflater.inflate(R.layout.fragment_base_food, container, false)
        vm = ViewModelProvider(requireActivity())[CountVM::class.java]
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(context)
        init()
    }

    fun init() {
        finalList = arrayListOf()
        if (NetworkTools().isAvailableConnection(requireContext())) {
            recyclerView = requireView().findViewById(R.id.recyclerView)
            adapter = FoodAdapter(dataToShow)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(context, 3)

            adapter.itemClickListener {
                val foodDetail = dataToShow.getOrNull(it)
                showLoading()
                if (vm.cartList.value != null) {
                    if (finalList.contains(foodDetail)) {
                        count += 1
                        foodDetail!!.count = count
                    } else {
                        val list = vm.cartList.value
                        if(list!!.contains(foodDetail)){
                            count = 1
                            foodDetail?.count = count + 1
                            finalList.add(foodDetail!!)
                        }else{
                            vm.cartList.value!!.add(foodDetail!!)
                        }
                    }
                } else {
                    if (finalList.contains(foodDetail)) {
                        count += 1
                        foodDetail!!.count = count
                    } else {
                        finalList.add(foodDetail!!)
                        vm.setFoodDetailList(finalList)
                    }
                }
                vm.increaseCount()
                hideLoading()


            }
        } else {
            hideAllViews()
            showAlert("Connect To Network")
        }

    }

    fun hideAllViews() {
        recyclerView = requireView().findViewById(R.id.recyclerView)
        recyclerView.visibility = View.GONE
    }

    fun showLoading(){
        progressDialog.setTitle("Entrees")
        progressDialog.setMessage("Adding Food to Cart")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
    fun hideLoading(){
        progressDialog.dismiss()
    }

    private fun filterDataByCategory(allData: List<FoodBO>, category: String): List<FoodBO> {
        return allData.filter { it.category == category }
    }


    companion object {
        private const val ARG_ALL_DATA = "all_data"
        private const val ARG_CATEGORY = "category"
        fun newInstance(allData: List<FoodBO>, category: String): BaseFoodFragment {
            return BaseFoodFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ALL_DATA, ArrayList(allData))
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }

    fun showAlert(err: String) {
        val layoutInflator = this.layoutInflater
        val dialogView = layoutInflator.inflate(R.layout.common_dialog_layout, null)
        dialog = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialogText = dialogView.findViewById<TextView>(R.id.titleDialog)
        val btn = dialogView.findViewById<Button>(R.id.btn_done)
        dialogText.text = err
        dialog.setCancelable(false)
        alert = dialog.create()
        alert.show()

        btn.setOnClickListener {
            alert.dismiss()
            init()
        }
    }

    override fun updateBadge(count: Int) {
    }
}