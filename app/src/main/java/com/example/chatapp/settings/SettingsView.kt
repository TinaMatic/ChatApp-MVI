package com.example.chatapp.settings

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface SettingsView: MvpView {

    /**
     * Load initial data
     */
    fun loadInitalData(): Observable<Unit>

    /**
     * Open Gallery to change picture
     */
    fun changePicture(): Observable<Unit>

    /**
     * Open change color screen
     */
    fun changeColor(): Observable<Unit>

    /**
     * Open change status screen
     */
    fun changeStatus(): Observable<Unit>

    /**
     * Open change name screen
     */
    fun changeName(): Observable<Unit>

    /**
     * Render the state in the UI
     */
    fun render(state: SettingsViewState)
}