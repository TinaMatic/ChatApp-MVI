package com.example.chatapp.color

import android.content.res.Resources
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ColorInteractor @Inject constructor(val resources: Resources, private val firebaseRepository: FirebaseRepository) {

    fun changeColor(color: String, colorName: String): Observable<ColorPartialState>{
        return firebaseRepository.changeColor(color)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it){
                    Observable.just(ColorPartialState.SuccessfulChange(resources.getString(R.string.successful_color_change) + " " + colorName),
                        ColorPartialState.SuccessfulChange(null))
                }else{
                    Observable.just(ColorPartialState.Error(resources.getString(R.string.error_color_change) + " " + colorName),
                        ColorPartialState.Error(null))
                }
            }.onErrorReturn { ColorPartialState.Error(resources.getString(R.string.error_color_change) + " " + colorName) }
    }
}