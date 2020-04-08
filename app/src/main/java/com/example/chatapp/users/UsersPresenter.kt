package com.example.chatapp.users

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UsersPresenter @Inject constructor(val interactor: UsersInteractor,
                                         private var openProfileScreenCallback:(()-> Unit)?,
                                         private var openChatScreenCallback: (() -> Unit)?): MviBasePresenter<UsersView, UsersViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var currentState: UsersViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = UsersViewState()

        val loadInitialData = intent(UsersView::loadInitialData).switchMap {
            interactor.loadInitialUsers()
        }

        val openDialog = intent(UsersView::openDialog).switchMap {
            interactor.openDialog(it.userId)
        }

        val viewState = Observable.merge(loadInitialData, openDialog)
            .scan(currentState, this::stateReducer)

        subscribeViewState(viewState, UsersView::render)

        compositeDisposable.add(intent(UsersView::openProfile).subscribe {
            openProfileScreenCallback!!()
        })

        compositeDisposable.add(intent(UsersView::openChat).subscribe {
            openChatScreenCallback!!()
        })
    }

    private fun stateReducer(previousState: UsersViewState, partialState: UsersPartialState): UsersViewState{
        currentState = when(partialState){
            is UsersPartialState.Loading -> previousState.copy(isLoading = true)
            is UsersPartialState.LoadInitialData -> previousState.copy(isLoading = false, users = partialState.users, showDialog = false)
            is UsersPartialState.OpenDialog -> previousState.copy(showDialog = true, userId = partialState.userId)
            else -> previousState.copy()
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openProfileScreenCallback = null
        openChatScreenCallback = null
    }
}