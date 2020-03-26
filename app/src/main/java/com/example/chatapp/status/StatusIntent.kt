package com.example.chatapp.status

sealed class StatusIntent {

    data class ChangeStatus(val status: String?): StatusIntent()
}