package com.example.chatapp.createAccount

sealed class CreateAccountIntent {

    object LoadPage: CreateAccountIntent()

    data class CreateAccount(val name: String, val email: String, val password: String): CreateAccountIntent()
}