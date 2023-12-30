package com.sdapps.entres.home.ordertaking.frag.presenter

import com.sdapps.entres.core.date.db.DBHandler
import com.sdapps.entres.home.ordertaking.frag.FoodBO

interface FoodActivityManager {

    interface View{

    }
    interface Presenter{
        fun attachView(view: View)
        fun getFoodMasterList(db: DBHandler): ArrayList<FoodBO>
    }
}