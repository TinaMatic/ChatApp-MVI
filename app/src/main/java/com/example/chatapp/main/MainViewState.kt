package com.example.chatapp.main

data class MainViewState(val isLoading: Boolean = false, val loginMessage: String? = null,
                         val isLoggedIn: Boolean = false)

sealed class MainPartialState {

    object Loading: MainPartialState()

    object InitialState: MainPartialState()

    object OpenLogin: MainPartialState()

    object OpenCreateAccount: MainPartialState()

    data class IsLoggedIn(val isLoggedIn: Boolean,val loginMessage: String?): MainPartialState()
}
