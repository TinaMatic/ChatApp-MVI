package com.example.chatapp.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface MainView: MvpView {

    /**
     *Screen start intent for setting initial UI
     */
    fun loadPageIntent(): Observable<Unit>

    /**
     * Check if user is logged in
     */
    val isLoggedIn: PublishSubject<Unit>

    /**
     * open login screen
     */
    fun openLogin(): Observable<Unit>

    /**
     * open create new profile screen
     */
    fun openCreateNewAccount(): Observable<Unit>

    /**
     * open dashboard screen
     */
    val openDashboard: Observable<Unit>

    /**
     * Renders the state in the UI
     */
    fun render(state: MainViewState)

}