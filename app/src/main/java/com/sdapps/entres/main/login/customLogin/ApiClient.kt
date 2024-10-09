package com.sdapps.entres.main.login.customLogin

import android.util.Log
import com.sdapps.entres.main.login.customLogin.model.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ApiClient {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json{
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun fetchData(): List<User> {
        val responseText = client.get("http://10.0.2.2:8080/users").bodyAsText().trim()
        println(responseText)
        val userList: List<User> = Json.decodeFromString(responseText)
        return userList
    }

    @OptIn(InternalAPI::class)
    suspend fun createUser(user: User): HttpResponse {
        val jsonBody = Json.encodeToString(user)

        Log.d("JSON", jsonBody)

        val response =  client.post("http://10.0.2.2:8080/addUsers") {
            contentType(ContentType.Application.Json)
            body = jsonBody
        }
        Log.d("REq", response.toString());

        return response
    }

}