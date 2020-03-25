package com.example.chatapp.settings

import android.content.res.Resources
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsInteractor @Inject constructor(private val firebaseRepository: FirebaseRepository, val resources: Resources) {

    fun loadData(): Observable<SettingsPartialState>{
        return firebaseRepository.loadCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(SettingsPartialState.InitialState(it.second))
                }else{
                    Observable.just(SettingsPartialState.Error)
                }
            }.startWith(SettingsPartialState.Loading)
            .onErrorReturn { SettingsPartialState.Error }
}

    fun changeImage(): Observable<SettingsPartialState>{
        return Observable.fromCallable { SettingsPartialState.Error }
    }
}