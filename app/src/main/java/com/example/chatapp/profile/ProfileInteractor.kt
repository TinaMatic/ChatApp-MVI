package com.example.chatapp.profile

import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileInteractor @Inject constructor(val firebaseRepository: FirebaseRepository) {

    fun loadUser(userId: String): Observable<ProfilePartialState>{
        return firebaseRepository.loadSingleUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(ProfilePartialState.InitialState(it.second))
                }else{
                    Observable.just(ProfilePartialState.Error)
                }
            }.startWith(ProfilePartialState.Loading)
            .onErrorReturn { ProfilePartialState.Error }
    }
}