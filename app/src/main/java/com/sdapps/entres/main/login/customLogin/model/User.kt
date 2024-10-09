package com.sdapps.entres.main.login.customLogin.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val usrname: String = "",
    val usremail: String = "",
    val usrpassword: String = "",
    val createddate: String = ""
)