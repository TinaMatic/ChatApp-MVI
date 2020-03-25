package com.example.chatapp.color

data class ColorViewState (val successfulMessage: String? = null, val errorMessage: String? = null)

sealed class ColorPartialState {

    data class SuccessfulChange(val successfulMessage: String?): ColorPartialState()

    data class Error(val errorMessage: String?): ColorPartialState()
}