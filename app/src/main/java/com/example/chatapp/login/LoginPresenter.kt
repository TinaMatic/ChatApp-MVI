package com.example.chatapp.login

import android.content.Context
import android.util.Log
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginPresenter @Inject constructor(val interactor: LoginInteractor,
                                         private var openDashboardScreenCallback: (() -> Unit)?) : MviBasePresenter<LoginView, LoginViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var currentState: LoginViewState

    override fun bindIntents() {

        compositeDisposable = CompositeDisposable()

        currentState = LoginViewState()

        val loginUser = intent(LoginView::login)
            .switchMap {
                interactor.loginUser(it.email, it.password)
            }.share()

        val viewStates = loginUser
            .scan(currentState, this::stateReducer)

        subscribeViewState(viewStates, LoginView::render)

        compositeDisposable.add(loginUser.filter {state->
            state is LoginPartialState.LoginSuccess
        }.subscribe ({ openDashboardScreenCallback!!() }
            ,{
            Log.d("Error opening dashboard", it.toString())
        }))
    }

     private fun stateReducer(previousState: LoginViewState, partialState: LoginPartialState): LoginViewState{
        currentState = when(partialState) {
            is LoginPartialState.Loading -> previousState.copy(isLoading = true)
            is LoginPartialState.InitialState -> previousState.copy()
            is LoginPartialState.ErrorMessagePassword -> previousState.copy(errorMessagePassword = partialState.errorMessagePassword, isLoading = false)
            is LoginPartialState.ErrorMessageEmail -> previousState.copy(errorMessageEmail = partialState.errorMessageEmail, isLoading = false)
            is LoginPartialState.LoginFailed -> previousState.copy(loginFailedMessage = partialState.loginFailedMessage, isLoading = false)
            else -> previousState.copy()
        }
         return currentState
    }

    override fun unbindIntents() {
        compositeDisposable.clear()
        openDashboardScreenCallback = null
    }
}