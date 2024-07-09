package com.sdapps.entres.main.login.view.compose
import android.content.Context
import android.util.Log
import android.util.Patterns
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
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.core.date.DateTools
import com.sdapps.entres.main.login.data.HotelBO
import com.sdapps.entres.main.login.data.LoginBO
import com.sdapps.entres.main.login.data.TaxBO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class LoginNewPresenter(private var view : LoginManagerCompose.View) : LoginManagerCompose.Presenter{

    private lateinit var context: Context
    private lateinit var db: DBHandler

    private lateinit var foodBOMaster: HotelBO
    private lateinit var masterMap: HashMap<*, *>

    private lateinit var masterItemList: MutableList<HotelBO.Items>

    private lateinit var taxMap: MutableMap<*,*>

    override fun attachView(view: LoginManagerCompose.View, context: Context, dbHandler: DBHandler) {
        this.view = view
        this.context = context
        this.db = dbHandler
    }

    override fun login(auth: FirebaseAuth, email: String, password: String) {
        if(isValidEmail(email) && isValidPassword(password)){
            proceedToLogin(auth,email,password)
        }else{
            view.showError("Unable to login")
        }
    }

    fun proceedToLogin(auth: FirebaseAuth, email: String, password: String){
        Log.d("FRB", "login start")
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { loginTask ->
            if(loginTask.isSuccessful){
                val currentUser = auth.currentUser?.uid
                CoroutineScope(
                    Dispatchers.Main
                ).launch {
                    getUserDetailsFromId(currentUser, true)
                }
            }else{
                view.hideLoading()
                loginTask.exception?.message?.let { view.showError(it) }
            }

        }

    }

    suspend fun getUserDetailsFromId(currentUserID: String?, isNewLogin: Boolean) {
        if (currentUserID != null) {

            if(isNewLogin){
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                dbRef.child(currentUserID).addListenerForSingleValueEvent(object :
                    ValueEventListener {

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
                            view.showError("Error getting details from firebase!")
                            view.hideLoading()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        view.showError(error.message)
                        view.hideLoading()
                        Log.d("FIREBASE", error.details)
                    }
                })
            }else{
                withContext(Dispatchers.Main) {
                    view.navigateToHome()
                }
            }
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

    private fun isValidEmail(email : String): Boolean{
        var isValid = false

        val emailFormat = if(email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }else{
            false
        }

        if((email.isNotBlank() || email.isNotEmpty()) && emailFormat  ){
            isValid = true
        }

        return isValid
    }
    private fun isValidPassword(password: String): Boolean{
        return password.length > 5
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
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

    fun QS(data: Any?): String {
        return "'$data'"
    }

    suspend fun insertDataIntoTaxMaster(taxBO: TaxBO){
        try {
            db.createDataBase()
            db.openDataBase()
            db.dbRawQuery("delete from TaxTable")

            val content = "${QT(taxBO.taxable.toString())}, ${QT(taxBO.taxType)}, ${QT(taxBO.taxRate.toString())}"
            db.insertSQL(tbl_taxTable, tbl_taxTableCols, content)
        }catch (ex: Exception){
            ex.printStackTrace()
        }

        CoroutineScope(Dispatchers.Main).launch {
            view.hideLoading()
            view.navigateToHome()
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
}