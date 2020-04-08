package com.example.chatapp.name

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class NamePresenter @Inject constructor(private val interactor: NameInteractor,
                                        private var openSettingsScreenCallback:(() -> Unit)?)
    : MviBasePresenter<NameView, NameViewState>() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var currentState: NameViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = NameViewState()

        val loadInitialName = intent(NameView::loadInitialName).switchMap {
            interactor.loadInitialName()
        }

        val changeName = intent(NameView::changeName).switchMap {
            interactor.changeName(it.name!!)
        }.share()

        val intents = Observable.merge(loadInitialName, changeName)
            .scan(currentState, this::stateReducer)

        subscribeViewState(intents, NameView::render)

        compositeDisposable.add(changeName.filter { state->
            state is NamePartialState.SuccessfulNameChange
        }.subscribe({
            openSettingsScreenCallback?.invoke()
        },{}))
    }


    private fun stateReducer(previousState: NameViewState, partialState: NamePartialState): NameViewState{
        currentState = when(partialState){
            is NamePartialState.InitialData -> previousState.copy(name = partialState.name)
            is NamePartialState.SuccessfulNameChange -> previousState.copy(successfulNameChange = partialState.successfulChangeMessage)
            is NamePartialState.Error -> previousState.copy(error = partialState.errorMessage)
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openSettingsScreenCallback = null
    }
}