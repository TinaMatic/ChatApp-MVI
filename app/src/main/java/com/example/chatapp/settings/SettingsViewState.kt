package com.example.chatapp.settings

import com.example.chatapp.models.Users

data class SettingsViewState(val isLoading: Boolean = false, val user: Users? = null,
                             val successfulMessage: String? = null, val errorMessage: String? = null)

sealed class SettingsPartialState{

    object Loading: SettingsPartialState()

    data class InitialState(val users: Users): SettingsPartialState()

    object Error: SettingsPartialState()

    data class SuccessfulPictureChange(val successfulMessage: String?): SettingsPartialState()

    data class ErrorPictureChange(val errorMessage: String?): SettingsPartialState()

    object OpenGallery: SettingsPartialState()

    object OpenChangeColor: SettingsPartialState()

    object OpenChangeStatus: SettingsPartialState()

    object OpenChangeName: SettingsPartialState()
}