package com.example.chatapp.color

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ColorView: MvpView {

    /**
     * Change the color of the user
     */
    val changeColor: PublishSubject<ColorIntent.ChangeColor>

    /**
     * Render the state of the UI
     */
    fun render(state: ColorViewState)
}