package com.example.chatapp.dashboard

data class DashboardViewState (val isLoading: Boolean = false, val errorMessage: String? = null)

sealed class DashboardPartialState{

    object Loading: DashboardPartialState()

    object OpenSettings: DashboardPartialState()

    object LogoutSuccess: DashboardPartialState()

    data class Error(val errorMessage: String?): DashboardPartialState()
}