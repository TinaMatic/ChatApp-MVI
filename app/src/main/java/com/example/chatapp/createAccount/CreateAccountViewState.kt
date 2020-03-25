package com.example.chatapp.createAccount

data class CreateAccountViewState(val isLoading: Boolean = false, val nameMessageError: String? = null,
                                  val emailMessageError: String? = null, val passwordMessageError: String? = null,
                                  val creataAccountFailedError: String? = null) {
}

sealed class CreateAccountPartialState{

    object Loading: CreateAccountPartialState()

    object InitalState: CreateAccountPartialState()

    data class NameMessageError(val nameMessageError: String?): CreateAccountPartialState()

    data class EmailMessageError(val emailMessageError: String?): CreateAccountPartialState()

    data class PasswordMessageError(val passwordMessageError: String?): CreateAccountPartialState()

    data class CreateAccountFailed(val createAccountError: String?): CreateAccountPartialState()

    object CreateAccountSuccess: CreateAccountPartialState()
}