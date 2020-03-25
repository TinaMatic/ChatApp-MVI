package com.example.chatapp.coordinator

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginCoordinator @Inject constructor(private val navigator: Navigator) {

    fun openDashboard(){
        navigator.showDashboardScreen()
    }
}