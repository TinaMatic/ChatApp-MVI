package com.example.chatapp.users

sealed class UsersIntent {

    data class OpenDialog(val userId: String): UsersIntent()
}