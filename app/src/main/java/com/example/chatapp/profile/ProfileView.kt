package com.example.chatapp.profile

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface ProfileView: MvpView {

    /**
     * Load data for the user
     */
    fun loadData(): Observable<ProfileIntent.LoadUser>

    /**
     * Open chat screen
     */
    fun openChat(): Observable<Unit>

    /**
     * Render the state of the UI
     */
    fun render(state: ProfileViewState)
}