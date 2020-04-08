package com.example.chatapp.main

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository)  {

    fun openLogin(): Observable<MainPartialState>{
        return Observable.just(MainPartialState.OpenLogin)
    }

    fun openCreateAccount(): Observable<MainPartialState>{
        return Observable.just(MainPartialState.OpenCreateAccount)
    }

    fun isUserLoggedIn(): Observable<MainPartialState>{
        return firebaseRepository.isUserLoggedIn()
            .switchMap {
                if(it){
                    Observable.just(MainPartialState.IsLoggedIn(true, null))
                }else{
                    Observable.just(MainPartialState.IsLoggedIn(false, resources.getString(R.string.not_logged_in)),
                        MainPartialState.IsLoggedIn(false, null))
                }
            }
    }

}