package com.example.chatapp.name

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface NameView: MvpView {

    /**
     * Load the initial name
     */
    fun loadInitialName(): Observable<Unit>

    /**
     * Change the name
     */
    fun changeName(): Observable<NameIntent.ChangeName>


    /**
     * Render the state of the UI
     */
    fun render(state: NameViewState)
}