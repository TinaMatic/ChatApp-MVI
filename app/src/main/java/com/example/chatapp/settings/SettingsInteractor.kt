package com.example.chatapp.settings

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsInteractor @Inject constructor(private val firebaseRepository: FirebaseRepository, val resources: Resources) {


    fun loadData(): Observable<SettingsPartialState>{
        return firebaseRepository.loadCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if(it.first){
                    Observable.just(SettingsPartialState.InitialState(it.second))
                }else{
                    Observable.just(SettingsPartialState.Error)
                }
            }.startWith(SettingsPartialState.Loading)
            .onErrorReturn { SettingsPartialState.Error }
}

    fun changeImage(resultUri: Uri?): Observable<SettingsPartialState>{
        return firebaseRepository.changePicture(resultUri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                if (it){
                    Observable.just(SettingsPartialState.SuccessfulPictureChange(resources.getString(R.string.successful_picture_change)),
                        SettingsPartialState.SuccessfulPictureChange(null))
                } else{
                    Observable.just(SettingsPartialState.ErrorPictureChange(resources.getString(R.string.error_picture_change)),
                        SettingsPartialState.ErrorPictureChange(null))
                }
            }.startWith(SettingsPartialState.Loading)
            .onErrorReturn { SettingsPartialState.ErrorPictureChange(resources.getString(R.string.error_message)) }
    }

    fun openGallery(): Observable<SettingsPartialState>{
        return Observable.just(SettingsPartialState.OpenGallery)
    }

    fun openChangeColor(): Observable<SettingsPartialState>{
        return Observable.just(SettingsPartialState.OpenChangeColor)
    }

    fun openChangeStatus(): Observable<SettingsPartialState>{
        return Observable.just(SettingsPartialState.OpenChangeStatus)
    }

    fun openChangeName(): Observable<SettingsPartialState>{
        return Observable.just(SettingsPartialState.OpenChangeName)
    }
}