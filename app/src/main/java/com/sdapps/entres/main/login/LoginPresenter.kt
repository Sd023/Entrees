package com.sdapps.entres.main.login

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.constants.DataMembers.tbl_foodDataMaster
import com.sdapps.entres.core.constants.DataMembers.tbl_foodMasterCols
import com.sdapps.entres.core.constants.DataMembers.tbl_taxTable
import com.sdapps.entres.core.constants.DataMembers.tbl_taxTableCols
import com.sdapps.entres.core.date.DateTools
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.login.data.HotelBO
import com.sdapps.entres.main.login.data.LoginBO
import com.sdapps.entres.main.login.data.TaxBO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class LoginPresenter : LoginHelper.Presenter {

    private lateinit var view: LoginHelper.View
    private lateinit var context: Context
    private lateinit var db: DBHandler

    private lateinit var foodBOMaster: HotelBO
    private lateinit var masterMap: HashMap<*, *>

    private lateinit var masterItemList: MutableList<HotelBO.Items>

    private lateinit var taxMap: MutableMap<*,*>

    override fun attachView(view: LoginHelper.View, context: Context, dbHandler: DBHandler) {
        this.view = view
        this.context = context
        this.db = dbHandler
    }

    override fun detachView() {
    }

    override fun login(firebaseAuth: FirebaseAuth, userName: String, password: String) {

            try {
                firebaseAuth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser = firebaseAuth.currentUser?.uid
                            CoroutineScope(Dispatchers.Main
                            ).launch {
                                getUserDetailsFromId(currentUser, true)
                            }
                        } else {
                            view.hideLoading()
                            view.showErrorDialog(task.exception?.message)
                        }
                    }
            } catch (ex: Exception) {
            view.hideLoading()
            Log.d("FIREBASE", ex.printStackTrace().toString())
            view.showErrorDialog(ex.message)
        }

    }

    fun QS(data: Any?): String {
        return "'$data'"
    }

    suspend fun getUserDetailsFromId(currentUserID: String?, isNewLogin: Boolean) {
        if (currentUserID != null) {

            if(isNewLogin){
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                dbRef.child(currentUserID).addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            val userData = snapshot.getValue(LoginBO::class.java)
                            val bo = LoginBO().apply {
                                currentUserUid = currentUserID
                                email = userData?.email
                                role = userData?.role
                                userId = userData?.userId
                                hotel = userData?.hotel
                                hotelBranch = userData?.hotelBranch
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                insertUserMasterRecords(bo)
                            }

                        } else {
                            view.showErrorDialog("Error getting details from firebase!")
                            view.hideLoading()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view.showErrorDialog(error.message)
                        view.hideLoading()
                        Log.d("FIREBASE", error.details)
                    }
                })
            }else{
                withContext(Dispatchers.Main) {
                    view.moveToNextScreen()
                }
            }
        }
    }

    override suspend fun register(firebaseAuth: FirebaseAuth, userName: String, password: String) {
        try {
            var role: String
            CoroutineScope(Dispatchers.IO).launch {
                if (userName.lowercase().contains("_m")) {
                    role = "manager"
                    firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.checkAndRegisterUser(role)
                            } else {
                                view.showErrorDialog(it.exception?.message)
                            }
                        }
                } else if (userName.lowercase().contains("_w")) {
                    role = "waiter"
                    firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.checkAndRegisterUser(role)
                            } else {
                                view.showErrorDialog(it.exception?.message)
                            }
                        }
                } else if (userName.lowercase().contains("_c")) {
                    role = "chef"
                    firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.checkAndRegisterUser(role)
                            } else {
                                view.showErrorDialog(it.exception?.message)
                            }
                        }
                }
            }


        } catch (ex: Exception) {
            ex.message
        }
    }


    suspend fun insertUserMasterRecords(bo: LoginBO){
        try {
            db.openDataBase()
            val content = StringBuilder()
                .append(QS(bo.currentUserUid!!))
                .append(",")
                .append(QS(bo.email))
                .append(",")
                .append(bo.userId)
                .append(",")
                .append(QS(bo.role))
                .append(",")
                .append(QS(bo.hotel))
                .append(",")
                .append(QS(bo.hotelBranch))
                .append(",")
                .append(QS(DateTools().now(DateTools.DATE_TIME)))
            db.insertSQL(DataMembers.tbl_masterUser, DataMembers.tbl_masterUserCols, content.toString())
            withContext(Dispatchers.Main) {
                downloadTheHotelData(bo)
            }


        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }


    fun downloadTheHotelData(bo: LoginBO) {
        CoroutineScope(Dispatchers.Main).launch {


        foodBOMaster = HotelBO()
        masterItemList = mutableListOf()
        taxMap = hashMapOf<Any,Any>()

            val hotelDBRef = FirebaseDatabase
                .getInstance()
                .getReference("hotels")
                .child(bo.hotel!!)

            hotelDBRef.child(bo.hotelBranch!!)
                .child("FoodMenu")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {
                            masterMap = (snapshot.value as? HashMap<*, *>)!!

                            for (key in masterMap.keys) {
                                val keyData = masterMap[key] as? HashMap<String, String>
                                if (keyData != null) {
                                    val items = HotelBO.Items(
                                        name = key.toString(),
                                        category = keyData["category"].toString(),
                                        id = keyData["id"].toString(),
                                        imgUrl = keyData["imgUrl"] ?: "",
                                        price = keyData["price"].toString()
                                    )
                                    masterItemList.add(items)
                                }
                            }
                            insertFoodDataToDB()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        print(error.message)
                    }
                })

        hotelDBRef.child(bo.hotelBranch!!).child("TaxMaster").addValueEventListener(
            object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        taxMap = (snapshot.value as? HashMap<*,*>)!!
                        val taxBO = TaxBO().apply {
                            taxable = (taxMap["isTaxable"]!! == "YES")
                            taxType = taxMap["taxType"] as String
                            taxRate = taxMap["taxRate"] as Long
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            insertDataIntoTaxMaster(taxBO)
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                   println(error.message)
                }
        })
        }




    }

    suspend fun insertDataIntoTaxMaster(taxBO: TaxBO){
        try {
            db.createDataBase()
            db.openDataBase()
            val content = "${QT(taxBO.taxable.toString())}, ${QT(taxBO.taxType)}, ${QT(taxBO.taxRate.toString())}"
            db.insertSQL(tbl_taxTable, tbl_taxTableCols, content)
        }catch (ex: Exception){
            ex.printStackTrace()
        }

        CoroutineScope(Dispatchers.Main).launch {
            view.hideLoading()
            view.moveToNextScreen()
        }

    }

    fun insertFoodDataToDB() {

        try {
            db.createDataBase()
            db.openDataBase()

            for (i in 0 until masterItemList.size) {
                val foodBO = masterItemList[i]
                val colValues = getValues(foodBO)
                db.insertSQL(tbl_foodDataMaster, tbl_foodMasterCols, colValues)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun getValues(data: HotelBO.Items): String {
        val sb = StringBuilder()
        sb.append(QT(data.id))
            .append(",")
            .append(QT(data.name))
            .append(",")
            .append(QT(data.category))
            .append(",")
            .append(QT(data.price))
            .append(",")
            .append(QT(data.imgUrl))
        return sb.toString()

    }

    fun QT(data: String?): String {
        return "'$data'"
    }

}