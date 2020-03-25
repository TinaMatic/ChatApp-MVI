package com.example.chatapp.login

import android.content.res.Resources
import android.text.TextUtils
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun loginUser(email:String, password: String): Observable<LoginPartialState>{

        return when {
            TextUtils.isEmpty(email) -> Observable.just(LoginPartialState.ErrorMessageEmail(resources.getString(R.string.email_mandatory))
                , LoginPartialState.ErrorMessageEmail(null))
            TextUtils.isEmpty(password) -> Observable.just(LoginPartialState.ErrorMessagePassword(resources.getString(R.string.password_mandatory)),
                LoginPartialState.ErrorMessagePassword(null))

                    else->{
                    firebaseRepository.loginUser(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .switchMap {
                            if(it){
                                Observable.just(LoginPartialState.LoginSuccess)
                            }
                            else{
                            Observable.timer(2, TimeUnit.SECONDS)
                                .map {
                                    LoginPartialState.LoginFailed(null)
                                }
                                .startWith(LoginPartialState.LoginFailed(resources.getString(R.string.login_failed)))
//                                Observable.just(LoginPartialState.LoginFailed(resources.getString(R.string.login_failed)), LoginPartialState.LoginFailed(null))
                            }
                        }.startWith(LoginPartialState.Loading)
                        .onErrorReturn{LoginPartialState.LoginFailed(resources.getString(R.string.login_failed))}
                    }
        }
    }

}
