package com.sdapps.entres.main.food.view.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.databinding.ActivityFoodListBinding
import com.sdapps.entres.main.food.BaseFoodFragment
import com.sdapps.entres.main.food.CardClickListener
import com.sdapps.entres.main.food.view.presenter.FoodActivityManager
import com.sdapps.entres.main.food.view.FoodBO
import com.sdapps.entres.main.food.view.presenter.FoodListPresenter


class FoodListActivity : AppCompatActivity(), FoodActivityManager.View , CardClickListener{

    private lateinit var binding: ActivityFoodListBinding
    private lateinit var adapter: SectionAdapter
    private lateinit var allList: ArrayList<FoodBO>

    private lateinit var presenter: FoodListPresenter
    private lateinit var db: DBHandler
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val bundle = intent?.extras
//        val str = bundle?.getString("SEAT")
//        val tableNumber = bundle?.getString("tableNumber")
        db  = DBHandler(applicationContext)
        presenter = FoodListPresenter(applicationContext)
        presenter.attachView(this)
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
      //binding.badgeCount.text = count.toString()
    }

    override fun onCardClick(count: Int) {
        Log.d("TAG","Voila! $count")

    }
}