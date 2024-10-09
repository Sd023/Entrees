package com.example.plugins

import com.example.dao.UserDaoImpl
import com.example.model.User
import com.example.model.Users
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Date
import kotlin.random.Random

fun Application.configureRouting() {
    routing {
        get("/users"){
            val users = UserDaoImpl().allUsers()
            call.respond(users)
        }

        get("/api") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }

        post("/addUsers") {
            val user = call.receive<User>()
            transaction {
                Users.insert { data ->
                    data[id] = user.id
                    data[usrname] = user.usrname
                    data[usremail] = user.usremail
                    data[usrpassword] = user.usrpassword
                    data[createddate] = user.createddate
                }
            }
            call.respondText("User Created",status = HttpStatusCode.Created)
        }
    }
}
