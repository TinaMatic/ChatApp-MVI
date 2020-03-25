package com.example.chatapp.settings

sealed class SettingsIntent {

    object LoadData: SettingsIntent()

    object OpenChangePicture: SettingsIntent()

    object OpenColor: SettingsIntent()

    object OpenStatus: SettingsIntent()

    object OpenChangeName: SettingsIntent()
}