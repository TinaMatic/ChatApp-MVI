package com.example.chatapp.coordinator

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardCoordinator @Inject constructor(private val navigator: Navigator){

    fun openSettings(){
        navigator.showSettingsScreen()
    }

    fun openMainScreen(){
        navigator.showMainScreen()
    }
}