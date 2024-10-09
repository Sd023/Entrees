package com.example.database

import com.example.model.Users
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    val dotenv = dotenv {
        ignoreIfMissing = true
    }
    val dbUser = dotenv["DB_USER"]
    val dbPassword = dotenv["DB_PASSWORD"]

    fun init(){
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/agaldb",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        newSuspendedTransaction(Dispatchers.IO){ block()}

}