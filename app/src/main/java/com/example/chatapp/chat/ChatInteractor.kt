package com.example.chatapp.chat

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Messages
import com.example.chatapp.models.Users
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun loadMessages(): Observable<ChatPartialState>{
        return firebaseRepository.loadMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(ChatPartialState.LoadInitialMessages(it.second))
                }else{
                    Observable.just(ChatPartialState.Error)
                }
            }.startWith(ChatPartialState.Loading)
            .onErrorReturn { ChatPartialState.Error }
        }

    fun loadUsers(): Observable<ChatPartialState>{
        return firebaseRepository.loadChatUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(!it.isNullOrEmpty()){
                    Observable.just(ChatPartialState.LoadInitialUsers(it))
                }else{
                    Observable.just(ChatPartialState.LoadInitialUsers(emptyList()))
                }
            }
    }

    fun sendMessage(message: Messages): Observable<ChatPartialState>{
        return firebaseRepository.sendMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(ChatPartialState.SuccessfulMessageSend)
                }else{
                    Observable.just(ChatPartialState.ErrorMessageSend)
                }
            }.startWith(ChatPartialState.Loading)
            .onErrorReturn { ChatPartialState.Error }
    }

    fun editMessage(message: String, messgageId: String): Observable<ChatPartialState>{
        return firebaseRepository.editMessage(message, messgageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(ChatPartialState.SuccessfulMessageEdit(resources.getString(R.string.successful_edit_message)),
                        ChatPartialState.SuccessfulMessageEdit(null))
                }else{
                    Observable.just(ChatPartialState.ErrorMessageEdit(resources.getString(R.string.error_edit_message)),
                        ChatPartialState.ErrorMessageEdit(null))
                }
            }.startWith(ChatPartialState.Loading)
            .onErrorReturn { ChatPartialState.Error }
    }

    fun deleteMessage(messageId: String): Observable<ChatPartialState>{
        return firebaseRepository.deleteMessage(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(ChatPartialState.SuccessfulDelete)
                }else{
                    Observable.just(ChatPartialState.Error)
                }
            }
    }

    fun openDialog(sendingMessageId: String): Observable<ChatPartialState>{
        return if (sendingMessageId.equals(firebaseRepository.currentUser?.uid)) {
            Observable.just(ChatPartialState.OpenDialog(true))
        } else {
            Observable.just(ChatPartialState.OpenDialog(false))
        }

//        return Observable.just(ChatPartialState.OpenDialog)
    }
}