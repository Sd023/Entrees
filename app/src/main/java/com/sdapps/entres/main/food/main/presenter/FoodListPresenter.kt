package com.sdapps.entres.main.food.main.presenter

import android.content.Context
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.food.main.FoodBO

class FoodListPresenter(val context: Context) : FoodActivityManager.Presenter {

    private lateinit var view: FoodActivityManager.View
    private lateinit var foodList: ArrayList<FoodBO>
    override fun attachView(view: FoodActivityManager.View) {
        this.view = view
    }

    override fun getFoodMasterList(db: DBHandler): ArrayList<FoodBO> {
        try {

            foodList = arrayListOf()
            db.openDataBase()
            val cursor = db.selectSQL("SELECT category,foodName,price,imgUrl from FoodDataMaster")

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val category = cursor.getString(0)
                    val foodName = cursor.getString(1)
                    val price = cursor.getInt(2)
                    val imgUrl = cursor.getString(3)
                    foodList.add(FoodBO(category, foodName,price, imgUrl,1,"",""))
                }
                cursor.close()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            return ArrayList()
        }

        return foodList
    }
}