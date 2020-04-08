package com.example.chatapp.settings

import android.util.Log
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingsPresenter @Inject constructor(val interactor: SettingsInteractor,
                                            private var openGalleryCallback: (() -> Unit)?,
                                            private var openChangeColorScreenCallback: (() -> Unit)?,
                                            private var openChangeStatusScreenCallback: (() -> Unit)?,
                                            private var openChangeNameScreenCallback: (() -> Unit)?) : MviBasePresenter<SettingsView, SettingsViewState>() {

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var currentState: SettingsViewState

    override fun bindIntents() {
        compositeDisposable = CompositeDisposable()

        currentState = SettingsViewState()

        val initialState = intent(SettingsView::loadInitalData)
            .switchMap {
                interactor.loadData()
            }

        val changeImage = intent(SettingsView::changePicture)
            .switchMap {
                interactor.changeImage(it.resultUri)
            }

        val openGallery = intent(SettingsView::openGallery).switchMap {
            interactor.openGallery()
        }

        val openChangeColor = intent(SettingsView::changeColor).switchMap {
            interactor.openChangeColor()
        }

        val openChangeStatus = intent(SettingsView::changeStatus).switchMap {
            interactor.openChangeStatus()
        }

        val openChangeName = intent(SettingsView::changeName).switchMap {
            interactor.openChangeName()
        }

        val allIntents = Observable.mergeArray(initialState, changeImage, openGallery,
            openChangeColor, openChangeStatus, openChangeName).share()


        subscribeViewState(allIntents.scan(currentState, this::stateReducer), SettingsView::render)

        compositeDisposable.add(allIntents.subscribeBy(onNext = {
            when(it){
                SettingsPartialState.OpenGallery -> openGalleryCallback?.invoke()
                SettingsPartialState.OpenChangeColor -> openChangeColorScreenCallback?.invoke()
                SettingsPartialState.OpenChangeStatus -> openChangeStatusScreenCallback?.invoke()
                SettingsPartialState.OpenChangeName -> openChangeNameScreenCallback?.invoke()
            }
        }, onError = {

        }))
    }

    private fun stateReducer(previousState: SettingsViewState, partialState: SettingsPartialState): SettingsViewState{
        currentState = when(partialState){
            is SettingsPartialState.Loading -> previousState.copy(isLoading = true)
            is SettingsPartialState.InitialState -> previousState.copy(isLoading = false, user = partialState.users )
            is SettingsPartialState.Error -> previousState.copy(isLoading = false)
            is SettingsPartialState.SuccessfulPictureChange -> previousState.copy(successfulMessage = partialState.successfulMessage)
            is SettingsPartialState.ErrorPictureChange -> previousState.copy(errorMessage = partialState.errorMessage)
            else -> previousState.copy()
        }

        return currentState
    }

    override fun unbindIntents() {
        super.unbindIntents()
        compositeDisposable.dispose()
        openGalleryCallback = null
        openChangeColorScreenCallback = null
        openChangeStatusScreenCallback = null
        openChangeNameScreenCallback = null
    }
}