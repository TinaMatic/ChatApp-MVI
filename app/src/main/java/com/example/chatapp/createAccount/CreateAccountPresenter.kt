package com.example.chatapp.createAccount

import android.util.Log
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CreateAccountPresenter @Inject constructor(val interactor: CreateAccountInteractor,
                                                 private var openDashboardScreenCallback: (() -> Unit)?) : MviBasePresenter<CreateAccountView, CreateAccountViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var currentState: CreateAccountViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = CreateAccountViewState()

        val createAccount = intent(CreateAccountView::createAccount)
            .switchMap {
                interactor.createAccount(it.email, it.password, it.name)
            }.share()

        val viewState = createAccount.scan(currentState, this::stateReducer)

        subscribeViewState(viewState, CreateAccountView::render)

        compositeDisposable.add(createAccount.filter {state->
            state is CreateAccountPartialState.CreateAccountSuccess
        }.subscribe({
            openDashboardScreenCallback?.invoke()
        },{
            Log.d("Error opening dashboard", it.toString())
        }))
    }

    private fun stateReducer(previousState: CreateAccountViewState, partialState: CreateAccountPartialState): CreateAccountViewState{
        currentState = when(partialState){
            is CreateAccountPartialState.Loading -> previousState.copy(isLoading = true)
            is CreateAccountPartialState.NameMessageError -> previousState.copy(nameMessageError = partialState.nameMessageError, isLoading = false)
            is CreateAccountPartialState.EmailMessageError -> previousState.copy(emailMessageError = partialState.emailMessageError, isLoading = false)
            is CreateAccountPartialState.PasswordMessageError -> previousState.copy(passwordMessageError = partialState.passwordMessageError, isLoading = false)
            is CreateAccountPartialState.CreateAccountFailed -> previousState.copy(creataAccountFailedError = partialState.createAccountError, isLoading = false)
            else -> previousState.copy()
        }
        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openDashboardScreenCallback = null
    }
}