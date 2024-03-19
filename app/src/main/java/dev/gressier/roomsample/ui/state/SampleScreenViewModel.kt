package dev.gressier.roomsample.ui.state

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gressier.roomsample.data.Dtos.AddMessageDto
import dev.gressier.roomsample.data.local.daos.MessageDao
import dev.gressier.roomsample.data.local.entities.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SampleScreenViewModel(private val messageDao: MessageDao, private val http: HttpClient) :
    ViewModel() {
    private val _entities = MutableStateFlow<List<Message>>(emptyList())
    val entities: Flow<List<Message>> = _entities
    val messages = mutableStateOf<List<Message>?>(null)

    init {
        getAllMessages()
        startFetchingMessages()
    }

    private fun getAllMessages() {
        viewModelScope.launch {
            try {
                val response: HttpResponse = http.get("https://api.3andm.com/v1/messages") {
                    header(
                        "Authorization",
                        "Bearer eyJraWQiOiIwbUZ4ZmFVN1J1TWh6T2RkUW9PWTUrcFpJb1I3Wmw1UVNWRHR3S1JNNElJPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJjNzIzYzViOS1mMTExLTRjOGMtYTM5Ny0yMTBmMWMyMzJhYTUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LXdlc3QtMS5hbWF6b25hd3MuY29tXC9ldS13ZXN0LTFfOUFXZWNDdU9wIiwiY29nbml0bzp1c2VybmFtZSI6ImM3MjNjNWI5LWYxMTEtNGM4Yy1hMzk3LTIxMGYxYzIzMmFhNSIsIm9yaWdpbl9qdGkiOiI0Zjk2NGU5ZC03MzQwLTRiYjMtOWUzZS1hMDA5NjY5MThlNGQiLCJhdWQiOiIyN2xhZ2FtMmg5bHFmYWMzcGFyOXU2YmhlMiIsImV2ZW50X2lkIjoiZWY5NTZiNDItY2YxNi00NWRlLWJmNmUtNTkzNjFmZWUzMTBjIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE3MTA4NDEzNTMsImV4cCI6MTcxMDkyNzc1MywiaWF0IjoxNzEwODQxMzUzLCJqdGkiOiI1M2RhZWFkMi04Mzk3LTQ0OGQtYWFhNS1iYTc0M2UwOWJlNTciLCJlbWFpbCI6Imp1cmplcmllQHN1cGluZm8uY29tIn0.qslQY5e_tCid2QD3lDMBLmhid6Zq4053j_Hc6Abb5iyuAfqwlapY5VcsOn_eJSoiFEkhgr7XQMrdliY0Csn0PWHO12ytrvni6Qd-5N8LzPWTUq7Dog5GoHZlezT-ju97WvPQhEx_HtM8DJaZffpZHrYumwIKZkP_rsoaRchWU9Oi_4l8yU4Tlvfy0KI4XY4OgrtdzB_3H0fzBMNDc1Erb3BmwpVC5cbmDWFONHdt7kHY1DtEOX4hEHtlBZW5ghUA5Tje_OHa-1Q9IXzws2GxIGV-BUK7dtpgxa_GOoCgTZ6AK1Lsx2gmV4d6xF4EbHVTRGCGYuQjSHKggIW5Ky4-CA"
                    )
                }
                val messageList: List<Message> = Json.decodeFromString(response.body<String>())
                messages.value = messageList.sortedBy { message: Message -> message.messageId }
            } catch (e: Exception) {
                Log.d("Erreur", e.toString())
                e.printStackTrace()
                // Gérer les erreurs ici
            }
        }
    }

    fun startFetchingMessages() {
        viewModelScope.launch {
            while (true) {
                // Récupérer les nouveaux messages
                getAllMessages()
                // Attendre un certain délai avant de récupérer à nouveau les messages
                delay(5000) // Par exemple, attendre 5 secondes
            }
        }
    }

    fun sendMessage(content: String) {
        val addMessageDto = AddMessageDto(content)

        viewModelScope.launch {
            try {
                val response: HttpResponse = http.post("https://api.3andm.com/v1/messages") {
                    header(
                        "Authorization",
                        "Bearer eyJraWQiOiIwbUZ4ZmFVN1J1TWh6T2RkUW9PWTUrcFpJb1I3Wmw1UVNWRHR3S1JNNElJPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJjNzIzYzViOS1mMTExLTRjOGMtYTM5Ny0yMTBmMWMyMzJhYTUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LXdlc3QtMS5hbWF6b25hd3MuY29tXC9ldS13ZXN0LTFfOUFXZWNDdU9wIiwiY29nbml0bzp1c2VybmFtZSI6ImM3MjNjNWI5LWYxMTEtNGM4Yy1hMzk3LTIxMGYxYzIzMmFhNSIsIm9yaWdpbl9qdGkiOiI0Zjk2NGU5ZC03MzQwLTRiYjMtOWUzZS1hMDA5NjY5MThlNGQiLCJhdWQiOiIyN2xhZ2FtMmg5bHFmYWMzcGFyOXU2YmhlMiIsImV2ZW50X2lkIjoiZWY5NTZiNDItY2YxNi00NWRlLWJmNmUtNTkzNjFmZWUzMTBjIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE3MTA4NDEzNTMsImV4cCI6MTcxMDkyNzc1MywiaWF0IjoxNzEwODQxMzUzLCJqdGkiOiI1M2RhZWFkMi04Mzk3LTQ0OGQtYWFhNS1iYTc0M2UwOWJlNTciLCJlbWFpbCI6Imp1cmplcmllQHN1cGluZm8uY29tIn0.qslQY5e_tCid2QD3lDMBLmhid6Zq4053j_Hc6Abb5iyuAfqwlapY5VcsOn_eJSoiFEkhgr7XQMrdliY0Csn0PWHO12ytrvni6Qd-5N8LzPWTUq7Dog5GoHZlezT-ju97WvPQhEx_HtM8DJaZffpZHrYumwIKZkP_rsoaRchWU9Oi_4l8yU4Tlvfy0KI4XY4OgrtdzB_3H0fzBMNDc1Erb3BmwpVC5cbmDWFONHdt7kHY1DtEOX4hEHtlBZW5ghUA5Tje_OHa-1Q9IXzws2GxIGV-BUK7dtpgxa_GOoCgTZ6AK1Lsx2gmV4d6xF4EbHVTRGCGYuQjSHKggIW5Ky4-CA"
                    )
                    contentType(ContentType.Application.Json)
                    setBody(addMessageDto)
                }
                val message: Message = Json.decodeFromString(response.body<String>())
                getAllMessages()
            } catch (e: Exception) {
                Log.d("Erreur", e.toString())
                e.printStackTrace()
            }
        }
    }
}