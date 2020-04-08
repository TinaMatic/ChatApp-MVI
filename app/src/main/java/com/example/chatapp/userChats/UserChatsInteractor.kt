package com.example.chatapp.userChats

import android.content.res.Resources
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Users
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserChatsInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun loadUsers(): Observable<UserChatsPartialState>{
        return firebaseRepository.loadAllChatUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.isNotEmpty()){
                    Observable.just(UserChatsPartialState.LoadUsers(it))
                }else{
                    Observable.just(UserChatsPartialState.LoadUsers(emptyList()))
                }
            }
    }

    fun openDialog(userId: String): Observable<UserChatsPartialState>{
        return Observable.just(UserChatsPartialState.OpenDialog(userId))
    }
}