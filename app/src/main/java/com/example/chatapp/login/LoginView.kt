package com.example.chatapp.login

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface LoginView: MvpView {
     /**
      *Screen start intent for setting initial UI
      */
    fun loadPageIntent(): Observable<Unit>

    /**
     * Login user
     */
    fun login(): Observable<LoginIntent.Login>

    /**
     * Renders the state in the UI
     */
    fun render(state: LoginViewState)
}