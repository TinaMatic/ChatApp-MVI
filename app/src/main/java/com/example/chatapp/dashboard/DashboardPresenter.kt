package com.example.chatapp.dashboard

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.math.log

class DashboardPresenter @Inject constructor(val interactor: DashboardInteractor,
                                             private var openSettingsScreenCallback: (() -> Unit)? ,
                                             private var openMainScreenCallback: (() -> Unit)?) : MviBasePresenter<DashboardView, DashboardViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var currentState: DashboardViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = DashboardViewState()

        val logout = intent(DashboardView::logout)
            .switchMap {
                interactor.logout()
            }.share()

        val viewStates = logout.scan(currentState, this::stateReducer)

        subscribeViewState(viewStates, DashboardView::render)

        compositeDisposable.add(logout.filter {state->
            state is DashboardPartialState.LogoutSuccess
        }.subscribe({
            openMainScreenCallback?.invoke()
        },{}))

        compositeDisposable.add(intent(DashboardView::openSettings).subscribe {
            openSettingsScreenCallback?.invoke()
        })
    }

    private fun stateReducer(previousState: DashboardViewState, partialState: DashboardPartialState): DashboardViewState{
       currentState = when(partialState){
           is DashboardPartialState.Loading -> previousState.copy(isLoading = true)
           is DashboardPartialState.Error -> previousState.copy(errorMessage = previousState.errorMessage)
           else -> currentState.copy(isLoading = false)
       }
        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openSettingsScreenCallback = null
        openMainScreenCallback = null
    }
}