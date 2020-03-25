package com.example.chatapp.main

data class MainViewState(val isLoading: Boolean = false, val loginMessage: String = "Not Signed In")

sealed class MainPartialState {

    object Loading: MainPartialState()

    object InitialState: MainPartialState()

    object OpenLogin: MainPartialState()

    object OpenCreateAccount: MainPartialState()
}
