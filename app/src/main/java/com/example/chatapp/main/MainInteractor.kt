package com.example.chatapp.main

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainInteractor @Inject constructor()  {

    fun openLogin(): Observable<MainPartialState>{
        return Observable.just(MainPartialState.OpenLogin)
    }

    fun openCreateAccount(): Observable<MainPartialState>{
        return Observable.just(MainPartialState.OpenCreateAccount)
    }

}