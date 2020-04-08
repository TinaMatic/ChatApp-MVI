package com.example.chatapp.profile

sealed class ProfileIntent{
    data class LoadUser(val userId: String): ProfileIntent()
}