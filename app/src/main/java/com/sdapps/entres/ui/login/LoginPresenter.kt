package com.sdapps.entres.ui.login

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.core.date.DataMembers
import com.sdapps.entres.core.date.DataMembers.tbl_foodDataMaster
import com.sdapps.entres.core.date.DataMembers.tbl_foodMasterCols
import com.sdapps.entres.core.date.DateTools
import com.sdapps.entres.core.date.db.DBHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class LoginPresenter : LoginHelper.Presenter {

    private lateinit var view: LoginHelper.View
    private lateinit var context: Context
    private lateinit var db: DBHandler

    private lateinit var foodBOMaster: HotelBO
    private lateinit var masterMap: HashMap<*, *>

    private lateinit var masterItemList: MutableList<HotelBO.Items>

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
                        getUserDetailsFromId(currentUser, true)
                    } else {
                        view.showErrorDialog(task.exception?.message)
                    }
                }
                .addOnCanceledListener {
                    view.showErrorDialog("Failed")
                }
                .addOnFailureListener {
                    view.showErrorDialog(it.message)
                }
        } catch (ex: Exception) {
            Log.d("FIREBASE", ex.printStackTrace().toString())
            view.showErrorDialog(ex.message)
        }
    }

    fun QS(data: Any?): String {
        return "'$data'"
    }

    fun getUserDetailsFromId(currentUserID: String?, isNewLogin: Boolean) {
        view.showLoading()
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
                            insertUserMasterRecords(bo)
                        } else {
                            view.showErrorDialog("Error getting details from firebase!")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view.showErrorDialog(error.message)
                        view.hideLoading()
                        Log.d("FIREBASE", error.details)
                    }
                })
            }else{
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
                                view.checkAndAuthorizeLogin(role)
                            } else {
                                view.showErrorDialog(it.exception?.message)
                            }
                        }
                } else if (userName.lowercase().contains("_w")) {
                    role = "waiter"
                    firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.checkAndAuthorizeLogin(role)
                            } else {
                                view.showErrorDialog(it.exception?.message)
                            }
                        }
                } else if (userName.lowercase().contains("_c")) {
                    role = "chef"
                    firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.checkAndAuthorizeLogin(role)
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


    fun insertUserMasterRecords(bo: LoginBO){
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
            downloadTheHotelData(bo)
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }


    fun downloadTheHotelData(bo: LoginBO) {
        foodBOMaster = HotelBO()
        masterItemList = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {

            val hotelDBRef = FirebaseDatabase
                .getInstance()
                .getReference("hotels")
                .child(bo.hotel!!)

            hotelDBRef.child(bo.hotelBranch!!)
                .child("FoodMenu")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot != null) {
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

                    }
                })

            CoroutineScope(Dispatchers.Main).launch {
                view.moveToNextScreen()
            }
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