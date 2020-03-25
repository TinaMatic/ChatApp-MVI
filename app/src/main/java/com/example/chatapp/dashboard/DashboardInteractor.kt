package com.example.chatapp.dashboard

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun logout(): Observable<DashboardPartialState>{
        return firebaseRepository.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(DashboardPartialState.LogoutSuccess)
                }else{
                    Observable.just(DashboardPartialState.Error(resources.getString(R.string.error_message)),
                        DashboardPartialState.Error(null))
                }
            }.startWith(DashboardPartialState.Loading)
            .onErrorReturn { DashboardPartialState.Error(resources.getString(R.string.error_message)) }
    }

//    fun openSettings(): Observable<DashboardPartialState>{
//        return Observable.just(DashboardPartialState.OpenSettings)
//    }
}