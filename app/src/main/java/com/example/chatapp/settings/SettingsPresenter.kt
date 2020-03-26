package com.example.chatapp.settings

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(val interactor: SettingsInteractor,
                                            private var openChangeColorScreenCallback: (() -> Unit)?,
                                            private var openChangeStatusScreenCallback: (() -> Unit)?,
                                            private var openChangeNameScreenCallback: (() -> Unit)?) : MviBasePresenter<SettingsView, SettingsViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var currentState: SettingsViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = SettingsViewState()

        val initialState = intent(SettingsView::loadInitalData).switchMap {
            interactor.loadData()
        }

        val changeImage = intent(SettingsView::changePicture).switchMap {
            interactor.changeImage()
        }


        val viewState = Observable.merge(initialState, changeImage)
            .scan(currentState, this::stateReducer)
//            initalState.scan(currentState, this::stateReducer)

        subscribeViewState(viewState, SettingsView::render)

        compositeDisposable.add(intent(SettingsView::changeColor).subscribe {
            openChangeColorScreenCallback!!()
        })

        compositeDisposable.add(intent(SettingsView::changeStatus).subscribe {
            openChangeStatusScreenCallback!!()
        })

        compositeDisposable.add(intent(SettingsView::changeName).subscribe {
            openChangeNameScreenCallback!!()
        })
    }

    private fun stateReducer(previousState: SettingsViewState, partialState: SettingsPartialState): SettingsViewState{
        currentState = when(partialState){
            is SettingsPartialState.Loading -> previousState.copy(isLoading = true)
            is SettingsPartialState.InitialState -> previousState.copy(isLoading = false, user = partialState.users )
            is SettingsPartialState.Error -> previousState.copy(isLoading = false)
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openChangeColorScreenCallback = null
        openChangeStatusScreenCallback = null
        openChangeNameScreenCallback = null
    }
}