package com.example.chatapp.status

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StatusInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun changeStaus(status: String): Observable<StatusPartialState>{
        return firebaseRepository.changeStatus(status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if (it){
                    Observable.just(StatusPartialState.SuccessfulStatusChange(resources.getString(R.string.successful_status_change)),
                        StatusPartialState.SuccessfulStatusChange(null))
                }else{
                    Observable.just(StatusPartialState.Error(resources.getString(R.string.error_status_change)),
                        StatusPartialState.Error(null))
                }
            }.onErrorReturn { StatusPartialState.Error(resources.getString(R.string.error_status_change)) }
    }

    fun loadInitialStatus(): Observable<StatusPartialState>{
        return firebaseRepository.loadCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(StatusPartialState.LoadInitialData(it.second.status))
                } else{
                    Observable.just(StatusPartialState.Error(resources.getString(R.string.error_status_change)),
                        StatusPartialState.Error(null))
                }
            }.onErrorReturn { StatusPartialState.Error(resources.getString(R.string.error_status_change)) }
    }
}