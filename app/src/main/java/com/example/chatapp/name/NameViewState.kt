package com.example.chatapp.name

data class NameViewState (val name: String? = null, val successfulNameChange: String? = null,
                     val error: String? = null)

sealed class NamePartialState{

    data class InitialData(val name: String?): NamePartialState()

    data class SuccessfulNameChange(val successfulChangeMessage: String?): NamePartialState()

    data class Error(val errorMessage: String?): NamePartialState()
}