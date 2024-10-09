package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable


@Serializable
data class User(val id : Int,
                val usrname: String,
                val usremail: String,
                val usrpassword: String,
                val createddate: String)


object Users: IntIdTable(){
    val usrname = varchar("usrname", 50)
    val usremail = varchar("usremail", 100)
    val usrpassword = varchar("usrpassword", 50)
    val createddate = varchar("createddate",20)

}