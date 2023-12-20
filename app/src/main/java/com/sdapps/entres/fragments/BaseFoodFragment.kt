package com.sdapps.entres.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.home.history.VM
import com.sdapps.entres.home.ordertaking.fragment.food.FoodBO
import com.sdapps.entres.interfaces.SearchListener


class BaseFoodFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    private lateinit var dataToShow: List<FoodBO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            val allData: List<FoodBO> = it.getParcelableArrayList(ARG_ALL_DATA) ?: emptyList()
            val category: String = it.getString(ARG_CATEGORY, "")

            dataToShow = filterDataByCategory(allData, category)
        }

        val view = inflater.inflate(R.layout.fragment_base_food, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        recyclerView = requireView().findViewById(R.id.recyclerView)
        adapter = FoodAdapter(dataToShow)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context,3)
    }


    private fun filterDataByCategory(allData: List<FoodBO>, category: String): List<FoodBO> {
        return allData.filter { it.cat == category }
    }


    companion object{
        private const val ARG_ALL_DATA = "all_data"
        private const val ARG_CATEGORY = "category"
        fun newInstance(allData: List<FoodBO>, category: String): BaseFoodFragment {
            return BaseFoodFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ALL_DATA, ArrayList(allData))
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }
}