package com.example.chatapp.userChats

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserChatsPresenter @Inject constructor(val interactor: UserChatsInteractor,
                                             private var openCharScreenCallback:(() ->Unit)?)
                                            :MviBasePresenter<UserChatsView, UserChatsViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var currentState: UserChatsViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = UserChatsViewState()

        val loadAllChatUsers = intent(UserChatsView::loadUsers).switchMap {
            interactor.loadUsers()
        }

        val openDialog = intent(UserChatsView::openDialog).switchMap {
            interactor.openDialog(it.userId)
        }

        val viewState = Observable.merge(loadAllChatUsers, openDialog)
            .scan(currentState, this::stateRedecuer)

        subscribeViewState(viewState, UserChatsView::render)

        compositeDisposable.add(intent(UserChatsView::openChat).subscribe {
            openCharScreenCallback!!()
        })
    }

    private fun stateRedecuer(previousState: UserChatsViewState, partialState: UserChatsPartialState): UserChatsViewState{
        currentState = when(partialState){
            is UserChatsPartialState.LoadUsers -> previousState.copy(chatUsers = partialState.chatUsers, shouldOpenDialog = false)
            is UserChatsPartialState.Loading -> previousState.copy(isLoading = true)
            is UserChatsPartialState.OpenDialog -> previousState.copy(shouldOpenDialog = true, userId = partialState.userId)
            is UserChatsPartialState.Error -> previousState.copy()
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openCharScreenCallback = null
    }
}