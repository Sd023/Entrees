package com.sdapps.entres.main.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.sdapps.entres.R
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.ActivityMainBinding
import com.sdapps.entres.main.home.tableview.dialog.CommonDialog
import com.sdapps.entres.main.home.tableview.tableFrag.presenter.TableViewManager
import com.sdapps.entres.main.home.tableview.tableFrag.presenter.TableViewPresenter
import com.sdapps.entres.main.home.tableview.tableFrag.view.TableViewAdapter
import com.sdapps.entres.main.homenew.HomeHostActivityNew
import com.sdapps.entres.network.NetworkTools


open class TableActivity : AppCompatActivity(),
    com.sdapps.entres.main.base.TableViewManager, TableViewManager.View{


    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: TableViewPresenter
    private lateinit var db: DBHandler
    private lateinit var customDia : AlertDialog.Builder
    private lateinit var customAlert: AlertDialog

    private lateinit var progressDialog: ProgressDialog


    companion object {
        val PROFILE = "profile"
        val ORDER_HISTORY = "ord_history"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.sdapps.entres.R.color.black)
        }
        progressDialog = ProgressDialog(this)
        presenter = TableViewPresenter(this)
        db = DBHandler(this)
        presenter.attachView(this, db)
        initializeFirebase()

        binding.profileView.setOnClickListener {
            switchToFragment(PROFILE)
        }

        binding.orderHistory.setOnClickListener {
            switchToFragment(ORDER_HISTORY)
        }

    }

    fun switchToFragment(tag: String){
        when(tag) {
            PROFILE -> {
                val intent = Intent(this@TableActivity, HomeHostActivityNew::class.java)
                intent.putExtra("frag_name", PROFILE)
                startActivity(intent)
            }

            ORDER_HISTORY -> {
                val intent =Intent(this@TableActivity, HomeHostActivityNew::class.java)
                intent.putExtra("frag_name", ORDER_HISTORY)
                startActivity(intent)
            }
            else -> showError("Unable to load data")
        }
    }

    override fun showError(msg: String) {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_LONG).show()
    }

    override fun showLoad() {
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Fetching Tables...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun hideLoad() {
        progressDialog.dismiss()
    }

    fun initializeFirebase() {
        if(NetworkTools().isAvailableConnection(applicationContext)){
            showLoad()
            presenter.loadUserDetails()
            presenter.tableRef()
        }else{
            showAlertDialog("Cannot connect to Network!")
        }
    }

    override fun showDialog(position: Int, tableName: String) {
        val customDia = CommonDialog(presenter)
        val args = Bundle()
        args.putInt("POSITION", position)
        args.putString("TABLENAME",tableName)
        customDia.arguments = args
        customDia.show(supportFragmentManager, CommonDialog.TAG)
    }

    override fun setupView(list: ArrayList<Int>, map: HashMap<Int, String>) {
        try {
            hideLoad()
            binding!!.recyclerView.layoutManager = GridLayoutManager(applicationContext, 3)
            val adapter = TableViewAdapter(list, map,applicationContext, this)
            binding!!.recyclerView.adapter = adapter
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun showAlertDialog(msg: String) {
        val layoutInflator = this.layoutInflater
        val dialogView = layoutInflator.inflate(R.layout.common_dialog_layout,null)
        customDia = AlertDialog.Builder(applicationContext).setView(dialogView)
        val dialogText = dialogView.findViewById<TextView>(R.id.titleDialog)
        val btn = dialogView.findViewById<Button>(R.id.btn_done)
        dialogText.text = msg
        customDia.setCancelable(false)
        customAlert = customDia.create()
        customAlert.show()

        btn.setOnClickListener{
            customAlert.dismiss()
            initializeFirebase()
        }
    }


}