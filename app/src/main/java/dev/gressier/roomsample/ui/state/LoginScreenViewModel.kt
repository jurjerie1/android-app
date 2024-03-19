package dev.gressier.roomsample.ui.state

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gressier.roomsample.data.Dtos.AddMessageDto
import dev.gressier.roomsample.data.Dtos.LoginDto
import dev.gressier.roomsample.data.local.entities.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

class LoginScreenViewModel(private val http: HttpClient, private val context: Context) :
    ViewModel() {
    fun login(email: String, password: String) {
        val loginDto = LoginDto(email = email, password = password)

        viewModelScope.launch {
            try {
                val response: HttpResponse = http.post("https://api.3andm.com/v1/auth/login") {
                    contentType(ContentType.Application.Json)
                    setBody(loginDto)
                }
                val token =
                    response.headers["Authorization"] // Récupérer le token depuis les headers
                token?.let {
                    // Enregistrer le token et l'email dans les SharedPreferences
                    saveUserCredentials(email, token)
                }
                // Traitement du message si nécessaire
            } catch (e: Exception) {
                // Gérer les erreurs ici
            }
        }
    }

    private fun saveUserCredentials(email: String, token: String) {
        val sharedPreferences =
            context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("token", token)
        editor.apply()
    }
}

