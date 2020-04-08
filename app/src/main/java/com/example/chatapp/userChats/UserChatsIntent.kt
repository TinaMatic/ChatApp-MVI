package com.example.chatapp.userChats

sealed class UserChatsIntent {

    data class OpenDialog(val userId: String): UserChatsIntent()
}