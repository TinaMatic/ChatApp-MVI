package com.example.chatapp.users

import com.example.chatapp.models.Users

data class UsersViewState(val isLoading: Boolean = false, val users: List<Users> = emptyList(),
                          val showDialog: Boolean = false, val userId: String? = null)

sealed class UsersPartialState{

    data class LoadInitialData(val users: List<Users>): UsersPartialState()

    object Loading: UsersPartialState()

    object Error: UsersPartialState()

    data class OpenDialog(val userId: String): UsersPartialState()

}