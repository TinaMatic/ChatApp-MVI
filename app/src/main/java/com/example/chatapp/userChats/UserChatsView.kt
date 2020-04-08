package com.example.chatapp.userChats

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface UserChatsView: MvpView {

    /**
     * Load initial users that have chatted before
     */
    val loadUsers: PublishSubject<Unit>

    /**
     * Open dialog
     */
    val openDialog: PublishSubject<UserChatsIntent.OpenDialog>

    /**
     * Open chat screen
     */
    val openChat: PublishSubject<Unit>

    /**
     * Render the state of the UI
     */
    fun render(state: UserChatsViewState)
}