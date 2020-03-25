package com.example.chatapp.coordinator

import javax.inject.Inject
import javax.inject.Singleton

class MainCoordinator @Inject constructor(private val navigator: Navigator) {

    fun openLogin(){
        navigator.showLoginScreen()
    }

    fun openCreateAccount(){
        navigator.showCreateAccountScreen()
    }

//    fun openDashboard(){
//        navigator.showDashboardScreen()
//    }
}