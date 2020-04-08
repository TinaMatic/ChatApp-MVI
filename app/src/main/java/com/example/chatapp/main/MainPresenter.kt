package com.example.chatapp.main

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter @Inject constructor (private val interactor: MainInteractor,
                                         private var openLoginScreenCallback: (() -> Unit)?,
                                         private var openCreateAccountCallback: (() -> Unit)?,
                                         private var openDashboardCallback:(() -> Unit)?): MviBasePresenter<MainView, MainViewState>(){

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var currentState: MainViewState

    override fun bindIntents() {

        compositeDisposable = CompositeDisposable()

        currentState = MainViewState()

        val loginIntent = intent(MainView::openLogin)
            .switchMap {
                interactor.openLogin()
            }

        val openCreateAccountIntent = intent(MainView::openCreateNewAccount)
            .switchMap {
                interactor.openCreateAccount()
            }

//        val isLoggedIn = intent(MainView::isLoggedIn)
//            .switchMap {
//                interactor.isUserLoggedIn()
//            }
//            intent(MainView::openCreateNewAccount).flatMap { Observable.just( MainPartialState.OpenCreateAccount, MainPartialState.InitialState) }

        val intents = Observable.merge(loginIntent, openCreateAccountIntent)
            .scan(currentState, this::stateReducer)

//        val sharedState = intents.share()

//        val viewState = sharedState.scan(currentState, this::stateReducer)

        subscribeViewState(intents, MainView::render)

        compositeDisposable.add(intent(MainView::openLogin).subscribe {
            openLoginScreenCallback!!()
        })

        compositeDisposable.add(intent(MainView::openCreateNewAccount).subscribe {
            openCreateAccountCallback!!()
        })

        compositeDisposable.add(intent(MainView::openDashboard).subscribe {
            openDashboardCallback!!()
        })



    }

    private fun stateReducer(previousState: MainViewState, partialState: MainPartialState): MainViewState{
        currentState = when(partialState){
            is MainPartialState.Loading -> previousState.copy(isLoading = true)
            is MainPartialState.InitialState -> previousState.copy(loginMessage = "Not Signed In")
//            is MainPartialState.IsLoggedIn -> previousState.copy(isLoggedIn = partialState.isLoggedIn, loginMessage = partialState.loginMessage)
            else -> previousState.copy()
        }

        return currentState
    }

    override fun unbindIntents() {
        compositeDisposable.dispose()
        openLoginScreenCallback = null
        openCreateAccountCallback = null
        openDashboardCallback = null
    }
}