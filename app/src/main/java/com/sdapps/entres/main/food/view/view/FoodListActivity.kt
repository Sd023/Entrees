package com.sdapps.entres.main.food.view.view

import android.os.Bundle
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
import com.sdapps.entres.main.food.dialog.CartViewDialog
import com.sdapps.entres.main.food.view.CountVM
import com.sdapps.entres.main.food.view.presenter.FoodActivityManager
import com.sdapps.entres.main.food.view.FoodBO
import com.sdapps.entres.main.food.view.presenter.FoodListPresenter


class FoodListActivity : AppCompatActivity(), FoodActivityManager.View {

    private lateinit var binding: ActivityFoodListBinding
    private lateinit var adapter: SectionAdapter
    private lateinit var allList: ArrayList<FoodBO>

    private lateinit var presenter: FoodListPresenter
    private lateinit var db: DBHandler
    private lateinit var vm : CountVM

    private lateinit var cart: RelativeLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var tableId: String
    private lateinit var seat : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle != null){
            tableId = bundle.getInt("tableNumber").toString()
            seat = bundle.getString("SEAT").toString()
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
        cartDialog.arguments = args
        cartDialog.show(supportFragmentManager,CartViewDialog.TAG)
    }

    fun setupViewPagerAndTab() {
        allList = getList()
        val categories: Set<String> = extractCategories(allList)


        adapter = SectionAdapter(supportFragmentManager)


        for (category in categories) {
            val frag = BaseFoodFragment.newInstance(
                filterDataByCategory(allList,category),
                category
            )
            adapter.addFragment(frag, category)
        }
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    fun extractCategories(list: ArrayList<FoodBO>): Set<String> {
        return list.map { it.category }.toSet()
    }


    private fun filterDataByCategory(
        allData: ArrayList<FoodBO>,
        category: String
    ): List<FoodBO> {
        return allData.filter { it.category == category }
    }


    fun getList(): ArrayList<FoodBO> {
        return presenter.getFoodMasterList(db)
    }


    private class SectionAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = mutableListOf<Fragment>()
        private val fragmentTitles = mutableListOf<String>()

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