package com.example.chatapp.chat

import com.example.chatapp.models.Messages
import com.example.chatapp.models.Users

data class ChatViewState (val messages: List<Messages> = emptyList(), val users: List<Users> = emptyList(),
                          val isLoading: Boolean = false, val successfulEditMessage: String? = null,
                          val errorEditMessage: String? = null, val showDialog: Boolean = false)

sealed class ChatPartialState{

    data class LoadInitialMessages(val messages: List<Messages>): ChatPartialState()

    data class LoadInitialUsers(val users: List<Users>): ChatPartialState()

    object Error: ChatPartialState()

    object Loading: ChatPartialState()

    object SuccessfulMessageSend: ChatPartialState()

    object ErrorMessageSend: ChatPartialState()

    data class SuccessfulMessageEdit(val successfulMessage: String?): ChatPartialState()

    data class ErrorMessageEdit(val errorMessage: String?): ChatPartialState()

    object SuccessfulDelete: ChatPartialState()

    data class OpenDialog(val showDialog: Boolean): ChatPartialState()

}