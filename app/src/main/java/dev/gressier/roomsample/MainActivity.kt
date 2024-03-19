package dev.gressier.roomsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.gressier.roomsample.data.local.AppDatabase
import dev.gressier.roomsample.data.local.daos.MessageDao
import dev.gressier.roomsample.ui.LoginScreen
import dev.gressier.roomsample.ui.SampleScreen
import dev.gressier.roomsample.ui.state.LoginScreenViewModel
import dev.gressier.roomsample.ui.state.SampleScreenViewModel
import dev.gressier.roomsample.ui.theme.RoomSampleTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val httpClient = HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val messageDao =
            AppDatabase.getDatabase(applicationContext).messageDao() // Correction de la variable
        val viewModel = SampleScreenViewModel(
            messageDao = messageDao,
            http = httpClient
        ) // Utilisation de la variable corrigée
        val LoginViewModel = LoginScreenViewModel(http = httpClient, context = applicationContext);

        setContent {
            RoomSampleTheme {
                //SampleScreen(viewModel = viewModel)
                LoginScreen(LoginViewModel)
            }
        }
    }
}
