package com.sdapps.entres.main.food.main.view

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sdapps.entres.R
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.ActivityFoodListBinding
import com.sdapps.entres.main.food.BaseFoodFragment
import com.sdapps.entres.main.food.main.vm.CartViewModel
import com.sdapps.entres.main.food.main.presenter.FoodActivityManager
import com.sdapps.entres.main.food.main.FoodBO
import com.sdapps.entres.main.food.main.presenter.FoodListPresenter
import com.sdapps.entres.main.food.main.vm.CartRepo
import com.sdapps.entres.main.food.main.vm.CartVMFactory


//Parent class for Whole Food Activity, Which consits of Tab and Recyclerview
//Category and Foodname data is filtered and category data is extracted used to inflate TabView.
// Each food will be filtered based on the category and inflated in separate Fragment
// RecyclerView logic should be handled in BaseFoodFragment & Adapter classes.

class FoodListActivity : AppCompatActivity(), FoodActivityManager.View {

    private lateinit var binding: ActivityFoodListBinding
    private lateinit var adapter: SectionAdapter
    private lateinit var masterDataList: ArrayList<FoodBO>

    private lateinit var presenter: FoodListPresenter
    private lateinit var db: DBHandler

    private lateinit var cart: RelativeLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var tableId: String
    private lateinit var seat: String
    private lateinit var tableName: String

    private lateinit var rightDrawerFragment: Fragment
    private lateinit var repo: CartRepo


    private val viewModel by lazy {
        repo = CartRepo(applicationContext)
        ViewModelProvider(this, CartVMFactory(repo))[CartViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFE", "onCreate drawer close")
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        if (bundle != null) {
            //data from tableView Common Dialog
            tableId = bundle.getInt("tableNumber").toString()
            seat = bundle.getString("SEAT").toString()
            tableName = bundle.getString("TABLENAME")!!

            Log.d("INTENT GET", "activity Created $tableId , $seat, $tableName")
        }
        rightDrawerFragment = CartDrawerFragment.newInstance(tableName, seat)
        rightDrawerFragment.arguments = bundle


        db = DBHandler(applicationContext)

        presenter = FoodListPresenter(applicationContext)
        presenter.attachView(this)
        setupViewPagerAndTab()

        viewModel.count.observe(this) {
            binding.count.text = it.toString()
        }
        binding.cartItem.setOnClickListener {
            openCart()
        }

    }


    fun openCart() {
        rightDrawerFragment = CartDrawerFragment()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val frag = fragmentManager.findFragmentByTag("cartDrawerFragment")
        if (frag != null) {
            transaction.remove(frag)
        }

        rightDrawerFragment = CartDrawerFragment.newInstance(tableName, seat)

        Log.d("INTENT", "Cart clicked $tableName,$seat")
        (rightDrawerFragment as CartDrawerFragment).closeDrawer(this)
        transaction.replace(R.id.rightFragView, rightDrawerFragment, "cartDrawerFragment").commit()
        binding.drawerLayout.openDrawer(findViewById(R.id.rightFragView))

    }

    fun setupViewPagerAndTab() {
        masterDataList = getFoodDataListFromDB()
        val categorySet: Set<String> = extractFoodNameBasedOnCategory(masterDataList)


        adapter = SectionAdapter(supportFragmentManager)


        if(presenter.getTaxRate() != null){
            for (category in categorySet) {
                val frag = BaseFoodFragment.newInstance(
                    filterFoodListByCategory(masterDataList, category),
                    category,presenter.getTaxRate()
                )
                adapter.addFragment(frag, category)
                // Category is added to TabView & Food list added as RecyclerView.
            }
        }else{
            Toast.makeText(applicationContext,"Tax is not available", Toast.LENGTH_LONG).show()
        }

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    fun extractFoodNameBasedOnCategory(list: ArrayList<FoodBO>): Set<String> {
        return list.map { it.category }.toSet()
    }


    private fun filterFoodListByCategory(
        allData: ArrayList<FoodBO>,
        category: String
    ): List<FoodBO> {
        return allData.filter { it.category == category }
    }


    fun getFoodDataListFromDB(): ArrayList<FoodBO> {
        return presenter.getFoodMasterList(db)
    }


    private class SectionAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = mutableListOf<Fragment>()
        private val fragmentTitles = mutableListOf<String>()

        //Tab view inflated here.
        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitles[position]

        }
    }

    override fun updateBadge(count: Int) {
    }

    override fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }
}