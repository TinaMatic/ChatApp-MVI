package com.example.chatapp.chat

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChatPresenter @Inject constructor(private val interactor: ChatInteractor): MviBasePresenter<ChatView, ChatViewState>() {

    lateinit var compositeDisposable: CompositeDisposable

    lateinit var currentState: ChatViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = ChatViewState()

        val loadInitialMessages = intent(ChatView::loadMessages).switchMap {
            interactor.loadMessages()
        }

        val loadInitialUsers = intent(ChatView::loadUsers).switchMap {
            interactor.loadUsers()
        }

        val sendMessage = intent(ChatView::sendMessage).switchMap {
            interactor.sendMessage(it.message)
        }

        val editMessage = intent(ChatView::editMessage).switchMap {
            interactor.editMessage(it.message, it.messageId)
        }

        val deleteMessage = intent(ChatView::deleteMessage).switchMap {
            interactor.deleteMessage(it.messageId)
        }

        val openDialog = intent(ChatView::openDialog).switchMap {
            interactor.openDialog(it.sendingUserId)
        }

        val viewState = Observable.mergeArray(loadInitialMessages, loadInitialUsers, sendMessage, editMessage, deleteMessage, openDialog)
            .scan(currentState, this::stateReducer)

        subscribeViewState(viewState, ChatView::render)
    }

    private fun stateReducer(previousState: ChatViewState, partialState: ChatPartialState): ChatViewState{
        currentState = when(partialState){
            is ChatPartialState.Loading -> previousState.copy(isLoading = true, showDialog = false)
            is ChatPartialState.LoadInitialMessages -> previousState.copy(messages = partialState.messages)
            is ChatPartialState.LoadInitialUsers -> previousState.copy(users = partialState.users)
            is ChatPartialState.SuccessfulMessageEdit -> previousState.copy(successfulEditMessage = partialState.successfulMessage, showDialog = false)
            is ChatPartialState.ErrorMessageEdit -> previousState.copy(errorEditMessage = partialState.errorMessage, showDialog = false)
            is ChatPartialState.OpenDialog -> previousState.copy(showDialog = partialState.showDialog)
            is ChatPartialState.SuccessfulDelete -> previousState.copy(showDialog = false)
            else -> previousState.copy(isLoading = false)
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
    }
}