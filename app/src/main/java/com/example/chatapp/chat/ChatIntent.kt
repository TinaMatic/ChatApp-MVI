package com.example.chatapp.chat

import com.example.chatapp.models.Messages

sealed class ChatIntent {

    data class SendMessage(val message: Messages): ChatIntent()

    data class EditMessage(val message: String, val messageId: String): ChatIntent()

    data class DeleteMessage(val messageId: String): ChatIntent()

    data class OpenDialog(val sendingUserId: String): ChatIntent()

}