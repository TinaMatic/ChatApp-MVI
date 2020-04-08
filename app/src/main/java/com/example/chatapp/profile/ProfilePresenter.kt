package com.example.chatapp.profile

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.disposables.CompositeDisposable
import java.lang.Error
import javax.inject.Inject

class ProfilePresenter @Inject constructor(val interactor: ProfileInteractor,
                                           private var openChatScreenCallback:(() -> Unit)?): MviBasePresenter<ProfileView, ProfileViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var currentState: ProfileViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = ProfileViewState()

        val loadInitialData = intent(ProfileView::loadData).switchMap {
            interactor.loadUser(it.userId)
        }

        val viewState = loadInitialData.scan(currentState, this::stateReducer)

        subscribeViewState(viewState, ProfileView::render)

        compositeDisposable.add(intent(ProfileView::openChat).subscribe {
            openChatScreenCallback!!()
        })

    }

    private fun stateReducer(previousState: ProfileViewState, partialState: ProfilePartialState): ProfileViewState{
        currentState = when(partialState){
            is ProfilePartialState.Loading -> previousState.copy(isLoading = true)
            is ProfilePartialState.InitialState -> previousState.copy(user = partialState.user, isLoading = false)
            is ProfilePartialState.Error -> previousState.copy()
        }
        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openChatScreenCallback = null
    }
}