package com.example.chatapp.users

import android.content.res.Resources
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun loadInitialUsers(): Observable<UsersPartialState>{
        return firebaseRepository.loadUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(UsersPartialState.LoadInitialData(it.second))
                }else{
                    Observable.just(UsersPartialState.Error)
                }
            }.startWith(UsersPartialState.Loading)
            .onErrorReturn { UsersPartialState.Error }
    }

    fun openDialog(userId: String): Observable<UsersPartialState>{
        return Observable.just(UsersPartialState.OpenDialog(userId))
    }
}