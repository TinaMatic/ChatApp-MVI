package com.example.chatapp.dashboard

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface DashboardView: MvpView {

    /**
     * Logout user
     */
    val logout: Observable<DashboardIntent.Logout>

    /**
     * Open settings screen
     */
    val openSettings: Observable<DashboardIntent.OpenSettings>

    /**
     * Renders the state in the UI
     */
    fun render(state: DashboardViewState)
}