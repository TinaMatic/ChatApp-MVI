package com.example.chatapp.coordinator

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersCoordinator @Inject constructor(private val navigator: Navigator) {

    fun openProfile(){
        navigator.showProfileScreen()
    }

    fun openChat(){
        navigator.showChatScreen()
    }
}