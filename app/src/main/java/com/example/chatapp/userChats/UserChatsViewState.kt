package com.example.chatapp.userChats

import com.example.chatapp.models.Users

data class UserChatsViewState (val isLoading: Boolean = false, val userId: String? = null,
                               val shouldOpenDialog: Boolean = false, val chatUsers: List<Users> = emptyList())

sealed class UserChatsPartialState{

    object Loading: UserChatsPartialState()

    data class LoadUsers(val chatUsers: List<Users>): UserChatsPartialState()

    data class OpenDialog(val userId: String?): UserChatsPartialState()

    object Error: UserChatsPartialState()
}