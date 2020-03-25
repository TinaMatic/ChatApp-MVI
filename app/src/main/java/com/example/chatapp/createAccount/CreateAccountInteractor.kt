package com.example.chatapp.createAccount

import android.content.res.Resources
import android.text.TextUtils
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateAccountInteractor @Inject constructor(val resources: Resources,
                                                  private val firebaseRepository: FirebaseRepository) {

    fun createAccount(email: String, password: String, displayName: String): Observable<CreateAccountPartialState>{
        return when{
            TextUtils.isEmpty(displayName) -> Observable.just(CreateAccountPartialState.NameMessageError(resources.getString(R.string.displayName_mandatory)),
                CreateAccountPartialState.NameMessageError(null))
            TextUtils.isEmpty(email) -> Observable.just(CreateAccountPartialState.EmailMessageError(resources.getString(R.string.email_mandatory)),
                CreateAccountPartialState.EmailMessageError(null))
            TextUtils.isEmpty(password) -> Observable.just(CreateAccountPartialState.PasswordMessageError(resources.getString(R.string.password_mandatory)),
                CreateAccountPartialState.PasswordMessageError(null))

            else ->{
                firebaseRepository.createAccount(email, password, displayName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap {
                        if(it){
                            Observable.just(CreateAccountPartialState.CreateAccountSuccess)
                        }else{
                            Observable.just(CreateAccountPartialState.CreateAccountFailed(resources.getString(R.string.create_account_failed)),
                                CreateAccountPartialState.CreateAccountFailed(null))
                        }
                    }.startWith(CreateAccountPartialState.Loading)
                    .onErrorReturn { CreateAccountPartialState.CreateAccountFailed(resources.getString(R.string.create_account_failed))}
            }
        }

    }
}