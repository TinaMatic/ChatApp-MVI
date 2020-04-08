package com.example.chatapp.users

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface UsersView: MvpView {

    /**
     * Load initial Users
     */
    val loadInitialData: PublishSubject<Unit>

    /**
     * Open dialog
     */
    val openDialog: PublishSubject<UsersIntent.OpenDialog>

    /**
     * Open profile screen
     */
    val openProfile: PublishSubject<Unit>

    /**
     * Open chat screen
     */
    val openChat: PublishSubject<Unit>

    /**
     * Render the state of the UI
     */
    fun render(state: UsersViewState)

}