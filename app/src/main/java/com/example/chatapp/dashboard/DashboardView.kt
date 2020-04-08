package com.example.chatapp.dashboard

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface DashboardView: MvpView {

    /**
     * Logout user
     */
    val logout: PublishSubject<Unit>

    /**
     * Open settings screen
     */
    val openSettings: PublishSubject<Unit>

    /**
     * Renders the state in the UI
     */
    fun render(state: DashboardViewState)
}