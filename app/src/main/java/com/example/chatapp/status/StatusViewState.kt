package com.example.chatapp.status

import com.example.chatapp.models.Users

data class StatusViewState(val errorMessage: String? = null, val successfulMessage: String? = null,
                           val status: String? = null)

sealed class StatusPartialState{

    data class LoadInitialData(val status: String?): StatusPartialState()

    data class SuccessfulStatusChange(val message: String?): StatusPartialState()

    data class Error(val errorMessage: String?): StatusPartialState()
}