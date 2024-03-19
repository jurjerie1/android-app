package dev.gressier.roomsample.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gressier.roomsample.data.local.entities.Message
import dev.gressier.roomsample.ui.state.SampleScreenViewModel
import androidx.compose.runtime.LaunchedEffect



@Composable
fun SampleScreen(viewModel: SampleScreenViewModel, listState: LazyListState = rememberLazyListState()) {
    val messages by viewModel.messages

    if (messages != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                items(messages!!) { message ->
                    MessageItem(message = message)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextBox(viewModel, listState)
        }

        // Scroll to bottom when messages change
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages!!.size - 1)
        }
    }
}

@Composable
fun TextBox(viewModel: SampleScreenViewModel, listState: LazyListState) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Enter your message") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    viewModel.sendMessage(text.toString())
                    text = ""
                    // Scroll to bottom when sending a message
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun MessageItem(message: Message) {
    val backgroundColor = if (message.userId == "jurjerie@supinfo.com") {
        Color(0xFFDCF8C6) // Couleur de fond pour les messages de l'utilisateur actuel
    } else {
        Color(0xFFF2F2F2) // Couleur de fond pour les messages des autres utilisateurs
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(backgroundColor)
            .padding(8.dp)
            .widthIn(max = 240.dp) // Largeur maximale pour le message
    ) {
        Text(
            text = message.userId,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black, // Couleur du nom de l'utilisateur
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = message.content,
            fontSize = 14.sp,
            color = Color.Black, // Couleur du contenu du message
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
fun TextBox(viewModel: SampleScreenViewModel) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Enter your message") },
            singleLine = true, // Définir le champ de texte comme une seule ligne
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send // Définir l'action de la touche "Entrée" comme "Envoyer"
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    (viewModel.sendMessage(text.toString()))
                    text = ""
                } // Appeler la fonction onSendMessage lorsque l'utilisateur appuie sur la touche "Entrée"
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
fun generateFakeMessages(): List<Message> {
    val messages = mutableListOf<Message>()
    repeat(10) { index ->
        val messageId = "message_$index"
        val content = "Contenu du message $index"
        val userId = "utilisateur_$index"
        messages.add(Message(messageId, content, userId))
    }
    return messages
}