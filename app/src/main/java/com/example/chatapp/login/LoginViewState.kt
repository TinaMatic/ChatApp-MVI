package com.example.chatapp.login

data class LoginViewState(val isLoading: Boolean = false, val errorMessageEmail: String? = null,
                          val errorMessagePassword: String? = null, val loginFailedMessage: String? = null)

sealed class LoginPartialState{

    object Loading: LoginPartialState()

    object InitialState: LoginPartialState()

    data class ErrorMessageEmail(val errorMessageEmail: String?): LoginPartialState()

    data class ErrorMessagePassword(val errorMessagePassword: String?): LoginPartialState()

    data class LoginFailed(val loginFailedMessage: String?): LoginPartialState()

    object LoginSuccess: LoginPartialState()
}