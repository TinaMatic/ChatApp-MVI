package com.example.chatapp.createAccount

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface CreateAccountView: MvpView {

    /**
    *Screen start intent for setting initial UI
    */
    fun loadPage(): Observable<Unit>

    /**
     * create new account
     */
    fun createAccount(): Observable<CreateAccountIntent.CreateAccount>

    /**
     * renders the state in the UI
     */
    fun render(state: CreateAccountViewState)
}