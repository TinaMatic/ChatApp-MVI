package com.example.chatapp.login

sealed class LoginIntent {

    object LoadPage: LoginIntent()

    data class Login(val email:String, val password: String): LoginIntent()
}