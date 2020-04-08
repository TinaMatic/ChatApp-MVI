package com.example.chatapp.profile

import com.example.chatapp.models.Users

data class ProfileViewState(val isLoading: Boolean = false, val user: Users? = null)

sealed class ProfilePartialState{

    object Loading: ProfilePartialState()

    data class InitialState(val user: Users): ProfilePartialState()

    object Error: ProfilePartialState()
}