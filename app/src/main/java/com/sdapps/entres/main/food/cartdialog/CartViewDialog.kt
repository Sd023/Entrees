package com.sdapps.entres.main.food.cartdialog

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.entres.BaseEntreesManager
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.FoodCartBinding
import com.sdapps.entres.main.food.main.CountVM
import com.sdapps.entres.main.food.main.FoodBO
import com.sdapps.entres.main.home.orderhistory.vm.OrderHistoryDataManager
import com.sdapps.entres.main.home.orderhistory.vm.OrderHistoryFactory
import com.sdapps.entres.main.home.orderhistory.vm.VM

class CartViewDialog (private val vm : CountVM): DialogFragment(){

    companion object {
        const val TAG = "FoodDialog"
    }

    private lateinit var binding: FoodCartBinding

    private lateinit var prg : ProgressDialog

    private lateinit var tableId : String
    private lateinit var seats : String
    private lateinit var tableName : String
    private lateinit var data : ArrayList<FoodBO>
    private lateinit var repo : CartRepo

    private val viewModel by lazy {
        repo = CartRepo(requireContext())
        ViewModelProvider(this, CartVMFactory(repo))[CartVM::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments
        prg = ProgressDialog(context)
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
            viewModel.insertDataToDB(data,tableName,seats)
        }
    }

    fun insertData(dialog: CartViewDialog,list: ArrayList<FoodBO>){

        try{
            dialog.dismiss()
            Toast.makeText(context,"LETS GO ", Toast.LENGTH_LONG).show()
            val db = DBHandler(requireContext())
            //(orderId TEXT, tableId TEXT, seatNumber TEXT, totalItems INT, totalOrderValue Double)


            val timestamp = System.currentTimeMillis()
            val userID = fetchUserId(db)

            val uid = StringBuilder().append(userID).append(timestamp)

            val sb =  StringBuilder()
                .append(QS(uid))
                .append(",")
                .append(QS(tableName))
                .append(",")
                .append(QS(seats))
                .append(",")
                .append(QS(data.size))
                .append(",")
                .append(QS(data.size))  //simple logic

            db.insertSQL(DataMembers.tbl_orderHeader, DataMembers.tbl_orderHeaderCols,sb.toString())

            for(orderDetail in list){

                //(orderId TEXT,foodName TEXT, qty INT,price DOUBLE, tableId TEXT,seatNumber TEXT, totalOrderValue DOUBLE)
                val orderDetails = StringBuilder()
                    .append(QS(uid))
                    .append(",")
                    .append(QS(orderDetail.foodName))
                    .append(",")
                    .append(QS(orderDetail.qty))
                    .append(",")
                    .append(QS(orderDetail.price))
                    .append(",")
                    .append(QS(tableName))
                    .append(",")
                    .append(QS(seats))
                    .append(",")
                    .append(QS(orderDetail.price))

                db.insertSQL(DataMembers.tbl_orderDetail,DataMembers.tbl_orderDetailCols,orderDetails.toString())
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    fun QS(data: Any):String{
        return "'$data'"
    }
    fun fetchUserId(db:DBHandler): Int{
        db.createDataBase()
        db.openDataBase()
        val sql = "select userId from MasterUser"
        val cursor = db.selectSQL(sql)
        if(cursor != null){
            while (cursor.moveToNext()){
                return cursor.getInt(0)
            }
        }
        return 0
    }

}