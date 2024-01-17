package com.sdapps.entres.main.food.main.view

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.ActivityFoodListBinding
import com.sdapps.entres.main.food.BaseFoodFragment
import com.sdapps.entres.main.food.cartdialog.CartViewDialog
import com.sdapps.entres.main.food.main.CountVM
import com.sdapps.entres.main.food.main.presenter.FoodActivityManager
import com.sdapps.entres.main.food.main.FoodBO
import com.sdapps.entres.main.food.main.presenter.FoodListPresenter



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
    private lateinit var vm : CountVM

    private lateinit var cart: RelativeLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var tableId: String
    private lateinit var seat : String
    private lateinit var tableName : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle != null){
            //data from tableView Common Dialog
            tableId = bundle.getInt("tableNumber").toString()
            seat = bundle.getString("SEAT").toString()
            tableName = bundle.getString("TABLENAME")!!

            Log.d("INTENT GET","activity Created $tableId , $seat, $tableName")
        }

        db  = DBHandler(applicationContext)
        vm = ViewModelProvider(this)[CountVM::class.java]

        presenter = FoodListPresenter(applicationContext)
        presenter.attachView(this)
        setupViewPagerAndTab()

        vm.count.observe(this) {
            binding.count.text = it.toString()
        }
        binding.cartItem.setOnClickListener {
            openCart()
        }

    }


    fun openCart(){
        val cartDialog = CartViewDialog(vm)
        val args = Bundle()
        args.putString("tableNumber", tableId)
        args.putString("SEAT",seat)
        args.putString("TABLENAME",tableName)
        Log.d("INTENT", "cart : $tableId , $seat , $tableName")
        cartDialog.arguments = args
        cartDialog.show(supportFragmentManager,CartViewDialog.TAG)
    }

    fun setupViewPagerAndTab() {
        masterDataList = getFoodDataListFromDB()
        val categorySet: Set<String> = extractFoodNameBasedOnCategory(masterDataList)


        adapter = SectionAdapter(supportFragmentManager)


        for (category in categorySet) {
            val frag = BaseFoodFragment.newInstance(
                filterFoodListByCategory(masterDataList,category),
                category
            )
            adapter.addFragment(frag, category)
            // Category is added to TabView & Food list added as RecyclerView.
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
}