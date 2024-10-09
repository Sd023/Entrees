package com.example.dao

import com.example.model.User

interface UserDao {

    suspend fun allUsers(): List<User>
}