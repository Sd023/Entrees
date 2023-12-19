package com.sdapps.entres.home.ordertaking.food

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


    private lateinit var viewModel : VM



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[VM::class.java]
        Log.d("New","4")
        //val bundle = intent?.extras
//        val str = bundle?.getString("SEAT")
//        val tableNumber = bundle?.getString("tableNumber")
        setupViewPagerAndTab()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("YourActivity", "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.top_app_bar, menu)
        val searchView: SearchView? = menu!!.findItem(R.id.searchIcon).actionView as SearchView?
        searchView!!.setIconifiedByDefault(false)
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("YourActivity", "onQueryTextChange: $newText")
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("YourActivity", "onQueryTextSubmit: $query")
                // Handle the submit action, if needed
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }





    fun setupViewPagerAndTab() {
        Log.d("New","5")
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