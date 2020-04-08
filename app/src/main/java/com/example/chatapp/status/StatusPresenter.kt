package com.example.chatapp.status

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StatusPresenter @Inject constructor(private val interactor: StatusInteractor,
                                          private var openSettingsScreenCallback:(() -> Unit)?)
    : MviBasePresenter<StatusView, StatusViewState>() {

    lateinit var compositeDisposable: CompositeDisposable

    lateinit var currentState: StatusViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = StatusViewState()

        val loadInitialData = intent(StatusView::loadInitialData).switchMap {
            interactor.loadInitialStatus()
        }

        val changeStatus = intent(StatusView::changeStatus).switchMap {
            interactor.changeStaus(it.status!!)
        }.share()

        val intents = Observable.merge(loadInitialData, changeStatus)
            .scan(currentState, this::stateReducer)

        subscribeViewState(intents, StatusView::render)

        compositeDisposable.add(changeStatus.filter { state->
            state is StatusPartialState.SuccessfulStatusChange
        }.subscribe({
            openSettingsScreenCallback?.invoke()
        },{}))
    }

    private fun stateReducer(previousState: StatusViewState, partialState: StatusPartialState): StatusViewState{
        currentState = when(partialState){
            is StatusPartialState.LoadInitialData -> previousState.copy(status = partialState.status)
            is StatusPartialState.SuccessfulStatusChange -> previousState.copy(successfulMessage = partialState.message)
            is StatusPartialState.Error -> previousState.copy(errorMessage = partialState.errorMessage)
        }
        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openSettingsScreenCallback = null
    }
}