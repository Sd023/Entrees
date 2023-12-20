package com.sdapps.entres.home.ordertaking.fragment.food

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.sdapps.entres.R
import com.sdapps.entres.databinding.ActivityFoodListBinding
import com.sdapps.entres.fragments.BaseFoodFragment
import com.sdapps.entres.home.history.VM


class FoodListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodListBinding
    private lateinit var adapter: SectionAdapter
    private lateinit var allList: ArrayList<FoodBO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val bundle = intent?.extras
//        val str = bundle?.getString("SEAT")
//        val tableNumber = bundle?.getString("tableNumber")
        setupViewPagerAndTab()

    }



    fun setupViewPagerAndTab() {
        allList = getList()
        val categories: Set<String> = extractCategories(allList)


        adapter = SectionAdapter(supportFragmentManager)


        for (category in categories) {
            val frag = BaseFoodFragment.newInstance(filterDataByCategory(allList,category), category)
            adapter.addFragment(frag, category)
        }
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    fun extractCategories(list: ArrayList<FoodBO>): Set<String> {
        return list.map { it.cat }.toSet()
    }


    private fun filterDataByCategory(
        allData: ArrayList<FoodBO>,
        category: String
    ): List<FoodBO> {
        return allData.filter { it.cat == category }
    }


    fun getList(): ArrayList<FoodBO> {
        return arrayListOf(
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("FOOD", "Pizza"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("DRINK", "Coffee"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Burger"),
            FoodBO("FOOD", "Fries"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("DRINK", "Tea"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FOOD", "Briyani"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Nethili"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("FISH", "Shark"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda"),
            FoodBO("DRINK", "Lemon Soda")
        )
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
}