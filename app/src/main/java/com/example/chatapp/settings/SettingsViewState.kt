package com.example.chatapp.settings

import com.example.chatapp.models.Users

data class SettingsViewState(val isLoading: Boolean = false, val user: Users? = null)

sealed class SettingsPartialState{

    object Loading: SettingsPartialState()

    data class InitialState(val users: Users): SettingsPartialState()

    object Error: SettingsPartialState()
}