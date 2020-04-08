package com.example.chatapp.name

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NameInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository){

    fun changeName(name: String): Observable<NamePartialState>{
        return firebaseRepository.changeName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(NamePartialState.SuccessfulNameChange(resources.getString(R.string.successful_name_change)),
                            NamePartialState.SuccessfulNameChange(null))
                }else{
                    Observable.just(NamePartialState.Error(resources.getString(R.string.error_name_change)),
                        NamePartialState.Error(null))
                }
            }.onErrorReturn { NamePartialState.Error(resources.getString(R.string.error_name_change)) }
    }

    fun loadInitialName(): Observable<NamePartialState>{
        return firebaseRepository.loadCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(NamePartialState.InitialData(it.second.displayName))
                }else{
                    Observable.just(NamePartialState.Error(resources.getString(R.string.error_name_change)),
                        NamePartialState.Error(null))
                }
            }.onErrorReturn { NamePartialState.Error(resources.getString(R.string.error_name_change)) }
    }
}