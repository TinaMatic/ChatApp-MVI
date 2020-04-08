package com.example.chatapp.coordinator

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsCoordinator @Inject constructor(private val navigator: Navigator){

    fun openChangeColor(){
        navigator.showChangeColorScreen()
    }

    fun openChangeStatus(){
        navigator.showChangeStatusScreen()
    }

    fun openChangeName(){
        navigator.showChangeNameScreen()
    }

    fun openGallery(){
        navigator.showGallery()
    }

    fun openSettings(){
        navigator.showSettingsScreen()
    }
}