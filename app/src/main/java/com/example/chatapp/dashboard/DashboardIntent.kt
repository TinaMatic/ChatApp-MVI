package com.example.chatapp.dashboard

sealed class DashboardIntent{

    object Logout: DashboardIntent()

    object OpenSettings: DashboardIntent()

}