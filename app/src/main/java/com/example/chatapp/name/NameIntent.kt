package com.example.chatapp.name

sealed class NameIntent {

    data class ChangeName(val name: String?): NameIntent()
}