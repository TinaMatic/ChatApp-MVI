package com.example.chatapp.status

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface StatusView: MvpView {

    /**
     * Load initial status
     */
    fun loadInitialData(): Observable<Unit>

    /**
     * Change status
     */
    fun changeStatus(): Observable<StatusIntent.ChangeStatus>

    /**
     * Render the state of the UI
     */
    fun render(state: StatusViewState)
}