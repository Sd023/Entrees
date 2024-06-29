package com.sdapps.entres.main.food

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Telephony.Mms.Rate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nex3z.notificationbadge.NotificationBadge
import com.sdapps.entres.R
import com.sdapps.entres.main.BaseEntreesFragment
import com.sdapps.entres.main.food.main.vm.CartViewModel
import com.sdapps.entres.main.food.main.FoodBO
import com.sdapps.entres.main.food.main.presenter.FoodActivityManager
import com.sdapps.entres.main.food.main.vm.CartRepo
import com.sdapps.entres.main.food.main.vm.CartVMFactory
import com.sdapps.entres.network.NetworkTools


//Base view  where food data is loaded in recyclerview.
// filtered based on the category
class BaseFoodFragment : BaseEntreesFragment(),FoodActivityManager.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    private lateinit var filteredFoodList: List<FoodBO>
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var alert: AlertDialog

    private lateinit var finalList: ArrayList<FoodBO>

    private lateinit var vm: CartViewModel

    private lateinit var badCount: NotificationBadge
    var qty = 1
    var totalOrderValue = 0.0
    private lateinit var progressDialog: ProgressDialog
    var taxRate: Float? = 0F
    var isTaxable: Boolean? = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        arguments?.let { arg ->
            val masterFoodData: List<FoodBO> = arg.getParcelableArrayList(MASTER_DATA) ?: emptyList()
            val masterCategory: String = arg.getString(MASTER_CATEGORY, "")
            filteredFoodList = filterDataByCategory(masterFoodData, masterCategory)
            taxRate = arg.getFloat(TAX_RATE)
            isTaxable = arg.getBoolean(TAXABLE)

        }

        val view = inflater.inflate(R.layout.fragment_base_food, container, false)
        vm = ViewModelProvider(requireActivity())[CartViewModel::class.java]
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
            adapter = FoodAdapter(filteredFoodList, taxRate!!, isTaxable!!)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(context, 3)

            adapter.itemClickListener { position ->
                vibrate()
                val foodDetail = filteredFoodList.getOrNull(position)

                if (vm.cartList.value != null) {
                    finalList = vm.cartList.value!!
                }
                if (finalList.isNotEmpty()) {
                    if (finalList.contains(foodDetail)) {
                        qty += 1
                        foodDetail!!.qty = qty
                        vm.calculateOrderValue(finalList)
                        foodDetail.totalOrderValue = vm.getOrderValue()!!
                    } else {
                        finalList.add(foodDetail!!)
                        vm.setFoodDetailList(finalList)
                        vm.calculateOrderValue(finalList)
                        foodDetail.totalOrderValue = vm.getOrderValue()!!
                    }
                } else {
                    finalList.add(foodDetail!!)
                    vm.setFoodDetailList(finalList)
                    vm.calculateOrderValue(finalList)
                    foodDetail.totalOrderValue = vm.getOrderValue()!!

                }
                vm.increaseCount()
                Toast.makeText(context,"Food Added to Cart",Toast.LENGTH_LONG).show()
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

    fun showLoading() {
        progressDialog.setTitle("Entrees")
        progressDialog.setMessage("Adding Food to Cart")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }


    fun hideLoading() {
        progressDialog.dismiss()
    }

    private fun filterDataByCategory(allData: List<FoodBO>, category: String): List<FoodBO> {
        return allData.filter { it.category == category }
    }


    companion object {
        private const val MASTER_DATA = "all_data"
        private const val MASTER_CATEGORY = "category"
        private const val TAX_RATE = "tax_rate"
        private const val TAXABLE = "isTaxable"

        fun newInstance(allData: List<FoodBO>, category: String, taxRate: Float, isTaxable: Boolean): BaseFoodFragment {
            return BaseFoodFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(MASTER_DATA, ArrayList(allData))
                    putString(MASTER_CATEGORY, category)
                    putFloat(TAX_RATE,taxRate)
                    putBoolean(TAXABLE,isTaxable)
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

    override fun closeDrawer() {

    }
}