package com.sdapps.entres.main.home.tableview.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sdapps.entres.main.food.view.view.FoodListActivity
import com.sdapps.entres.databinding.CommonDialogTableViewBinding
import com.sdapps.entres.main.home.tableview.tableFrag.presenter.TableViewPresenter
import com.sdapps.entres.main.login.data.HotelBO

class CommonDialog(var presenter: TableViewPresenter) : DialogFragment(), CommonDialogView.View {
    companion object {
        const val TAG = "CommonDialog"
    }

    private lateinit var cardAdapter: CommonDialogAdapter
    private lateinit var data: ArrayList<String>
    private lateinit var binding: CommonDialogTableViewBinding
    private var position: Int? = null
    var tablText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mArgs = arguments
        position = mArgs?.getInt("POSITION")
        binding = CommonDialogTableViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAll()
    }

    fun initAll() {
        tablText = "Table$position"
        binding.tblTxt.text = tablText
        try {
            if (presenter != null) {
                presenter.getSeatsReference(this)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }


//        data = arrayListOf("A", "B")
        /* cardAdapter = CommonDialogAdapter(data,*//*object : CommonDialogAdapter.OnLastItemClickListener{
                override fun onLastItemClick(lastPos: Int) {
                    addNewElement(lastPos)
                }
            }*//*this)*/


    }

    /* fun addNewElement(lastPos: Int) {
         val alphabets = arrayListOf("+")
         if (lastPos < alphabets.lastIndex) {
             data.add(alphabets[lastPos + 1])
         } else {
             Toast.makeText(context, "Maximum Seats Exceeded!", Toast.LENGTH_LONG).show()
         }
         cardAdapter.notifyDataSetChanged()
     }*/

    override fun switchActivity(position: Int, seat: String) {
        val intent = Intent(context, FoodListActivity::class.java)
        intent.putExtra("SEAT", seat)
        intent.putExtra("tableNumber", position)
        startActivity(intent)

    }

    override fun setupView(map: HashMap<String, ArrayList<HotelBO.Seats>>?) {
        val list = map?.get(tablText)
        cardAdapter = CommonDialogAdapter(list, this)
        binding.recyclerView.adapter = cardAdapter
    }
}