package com.example.chatapp.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView: MvpView {

    /**
     *Screen start intent for setting initial UI
     */
    fun loadPageIntent(): Observable<MainIntent.LoadPage>

    /**
     * open login screen
     */
    fun openLogin(): Observable<MainIntent.OpenLogin>

    /**
     * open create new profile screen
     */
    fun openCreateNewAccount(): Observable<MainIntent.OpenCreateNewProfile>

    /**
     * open dashboard screen
     */
    val openDashboard: Observable<MainIntent.OpenDashboard>

    /**
     * Renders the state in the UI
     */
    fun render(state: MainViewState)

}