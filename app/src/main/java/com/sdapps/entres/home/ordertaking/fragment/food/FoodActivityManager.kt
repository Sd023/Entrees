package com.sdapps.entres.home.ordertaking.fragment.food

import com.sdapps.entres.core.date.db.DBHandler

interface FoodActivityManager {

    interface View{

    }
    interface Presenter{
        fun attachView(view: FoodActivityManager.View)
        fun getFoodMasterList(db: DBHandler): ArrayList<FoodBO>
    }
}