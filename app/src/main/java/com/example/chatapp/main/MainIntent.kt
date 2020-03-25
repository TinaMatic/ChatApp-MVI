package com.example.chatapp.main

sealed class MainIntent {

    object LoadPage: MainIntent()

    object OpenCreateNewProfile: MainIntent()

    object OpenLogin: MainIntent()

    object OpenDashboard: MainIntent()
}