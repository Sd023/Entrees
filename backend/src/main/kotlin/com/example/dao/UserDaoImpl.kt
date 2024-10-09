package com.example.dao

import com.example.database.DatabaseFactory.dbQuery
import com.example.model.User
import com.example.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserDaoImpl: UserDao {

    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map { resultRowToUser(it) }
    }

    fun resultRowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            usrname = row[Users.usrname],
            usremail = row[Users.usremail],
            usrpassword = row[Users.usrpassword],
            createddate = row[Users.createddate],

        )
    }
}