package com.sdapps.entres.main.food.main.presenter

import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.food.main.FoodBO

interface FoodActivityManager {

    interface View{
        fun updateBadge(count: Int)
        fun closeDrawer()

    }
    interface Presenter{
        fun attachView(view: View)
        fun getFoodMasterList(db: DBHandler): ArrayList<FoodBO>
    }
}