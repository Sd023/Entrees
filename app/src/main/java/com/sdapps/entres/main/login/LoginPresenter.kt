package com.sdapps.entres.main.login

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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

    private lateinit var taxMap: MutableMap<*, *>

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
                        downloadMasterDataFromUser(currentUser, true)
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



    fun downloadMasterDataFromUser(currentUserID: String?, isNewLogin: Boolean){
        fetchUserRecords(currentUserID, isNewLogin) { loginBO ->
            insertUserMasterRecords(loginBO)
            downloadTheHotelData(loginBO)
        }
    }

    private fun fetchUserRecords(currentUserID: String?, isNewLogin: Boolean, callback: (LoginBO) -> Unit){
        if (currentUserID != null) {

            if (isNewLogin) {
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                dbRef.child(currentUserID)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.exists()) {

                                val userData = snapshot.getValue(LoginBO::class.java)
                                val bo = LoginBO().apply {
                                    currentUserUid = currentUserID // user session id not maintained in Firebase.
                                    email = userData?.email
                                    role = userData?.role
                                    userId = userData?.userId
                                    hotel = userData?.hotel
                                    hotelBranch = userData?.hotelBranch
                                }
                               callback(bo)

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
            } else {
                view.moveToNextScreen()

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


    private fun insertUserMasterRecords(bo: LoginBO) {
        try {
            db.openDataBase()
            val content = StringBuilder()
                .append(QT(bo.currentUserUid!!))
                .append(",")
                .append(QT(bo.email))
                .append(",")
                .append(bo.userId)
                .append(",")
                .append(QT(bo.role))
                .append(",")
                .append(QT(bo.hotel))
                .append(",")
                .append(QT(bo.hotelBranch))
                .append(",")
                .append(QT(DateTools().now(DateTools.DATE_TIME)))
                .append(",")
                .append(QT(""))
                .append(",")
                .append(QT(""))
            db.insertSQL(
                DataMembers.tbl_masterUser,
                DataMembers.tbl_masterUserCols,
                content.toString()
            )

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun getHotelIds(bo: LoginBO,firebaseDBRef: DatabaseReference) {
        fetchHotelId(firebaseDBRef) { hotelID ->
            fetchBranchId(bo,firebaseDBRef) { branchId ->
                updateIdsInTable(bo,hotelID,branchId)
            }
        }
    }

    private fun fetchHotelId(firebaseDBRef: DatabaseReference,callback : (Int) -> Unit) {
        firebaseDBRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                   val hotelId = (snapshot.value as? HashMap<*, *>)?.getOrDefault("id", 1) as? Long ?: 1L
                    callback(hotelId.toInt())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.details)
            }
        })
    }

    private fun fetchBranchId(bo: LoginBO,firebaseDBRef: DatabaseReference, callback: (Int) -> Unit){
        firebaseDBRef.child(bo.hotelBranch!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val hotelBranchId = (snapshot.value as? HashMap<*, *>)?.getOrDefault("id", 1) as? Long ?: 1L
                    callback(hotelBranchId.toInt())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG",error.details)
            }

        })
    }


    private fun getHotelData(bo: LoginBO,firebaseDBRef: DatabaseReference){
        fetchFoodMenu(bo,firebaseDBRef) { masterItemList ->
            fetchTaxDetails(bo,firebaseDBRef) { taxBO ->
                insertFoodDataToDB(masterItemList)
                insertDataIntoTaxMaster(taxBO)
                getHotelIds(bo,firebaseDBRef)
            }
        }
    }

    private fun fetchFoodMenu(bo: LoginBO,firebaseDBRef: DatabaseReference,callback : (MutableList<HotelBO.Items>) -> Unit){
        firebaseDBRef.child(bo.hotelBranch!!).child("FoodMenu").addValueEventListener(object : ValueEventListener {
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
                   callback(masterItemList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })
    }
    private fun fetchTaxDetails(bo: LoginBO,firebaseDBRef: DatabaseReference, callback: (TaxBO) -> Unit) {
        firebaseDBRef.child(bo.hotelBranch!!).child("TaxMaster").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    taxMap = (snapshot.value as? HashMap<*, *>)!!
                    val taxBO = TaxBO().apply {
                        taxable = (taxMap["isTaxable"]!! == "YES")
                        taxType = taxMap["taxType"] as String
                        taxRate = taxMap["taxRate"] as Long
                    }
                    callback(taxBO)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    }

    private fun downloadTheHotelData(bo: LoginBO) {

        foodBOMaster = HotelBO()
        masterItemList = mutableListOf()
        taxMap = hashMapOf<Any, Any>()
        val hotelDBRefs =
            FirebaseDatabase.getInstance().getReference("hotels").child(bo.hotel!!)
        getHotelData(bo,hotelDBRefs)


    }

    private fun updateIdsInTable(bo: LoginBO,hotelId: Int, hotelBranchId : Int){

        try{
            db.createDataBase()
            db.openDataBase()
            db.writableDatabase
            db.updateSQL("update MasterUser set hotelId= $hotelId where hotel = ${QT(bo.currentUserUid)}")
            db.updateSQL("update MasterUser set hotelBranchId= $hotelBranchId where hotel = ${QT(bo.currentUserUid)}")

        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun insertDataIntoTaxMaster(taxBO: TaxBO) {
        try {
            db.createDataBase()
            db.openDataBase()
            db.dbRawQuery("delete from TaxTable")

            val content =
                "${QT(taxBO.taxable.toString())}, ${QT(taxBO.taxType)}, ${QT(taxBO.taxRate.toString())}"
            db.insertSQL(tbl_taxTable, tbl_taxTableCols, content)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        CoroutineScope(Dispatchers.Main).launch {
            view.hideLoading()
            view.moveToNextScreen()
        }

    }

    private fun insertFoodDataToDB(list: MutableList<HotelBO.Items> ) {

        try {
            db.createDataBase()
            db.openDataBase()

            for (i in 0 until list.size) {
                val foodBO = list[i]
                val colValues = getValues(foodBO)
                db.insertSQL(tbl_foodDataMaster, tbl_foodMasterCols, colValues)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun getValues(data: HotelBO.Items): String {
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

    private fun QT(data: Any?): String {
        return "'$data'"
    }

}