package com.example.chatapp.color

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ColorPresenter @Inject constructor(private val interactor: ColorInteractor): MviBasePresenter<ColorView, ColorViewState>() {

    lateinit var compositeDisposable: CompositeDisposable

    lateinit var currentState: ColorViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = ColorViewState()

        val colorChange = intent(ColorView::changeColor)
            .switchMap {
                interactor.changeColor(it.color, it.colorName)
            }

        val intents = colorChange.scan(currentState, this::stateRedecuer)

        subscribeViewState(intents, ColorView::render)
    }

    private fun stateRedecuer(previousState: ColorViewState, partialState: ColorPartialState): ColorViewState{
        currentState = when(partialState){
            is ColorPartialState.SuccessfulChange -> previousState.copy(successfulMessage = partialState.successfulMessage)
            is ColorPartialState.Error -> previousState.copy(errorMessage = partialState.errorMessage)
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
    }
}