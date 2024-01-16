package com.sdapps.entres.core.constants

object DataMembers {

     const val tbl_masterUser = "MasterUser"
     const val tbl_masterUserCols = "uid,email,userId,role,hotel,hotelBranch,createdDate"

     const val tbl_foodDataMaster = "FoodDataMaster"
     const val tbl_foodMasterCols = "id,foodName,category,price,imgUrl"

     const val tbl_tableMaster = "TableMaster"
     const val tbl_tableMasterCols = "tableId,tableName,isStatus"

     const val tbl_tableSeatMapping = "TableSeatMapping"
     const val tbl_tableSeatMappingCols = "tableId,tableName,seatNum"

     const val tbl_orderHeader = "OrderHeader"
     const val tbl_orderHeaderCols = "orderId,tableId,seatNumber,totalItems,totalOrderValue"

     const val tbl_orderDetail = "OrderDetail"
     const val tbl_orderDetailCols = "orderId,foodName,qty,price,tableId,seatNumber,totalOrderValue"

}