package com.example.chatapp.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.SettingsCoordinator
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingsActivity : MviActivity<SettingsView, SettingsPresenter>(), SettingsView {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var settingsCoordinator: SettingsCoordinator

    @Inject
    lateinit var interactor: SettingsInteractor

    val GALLERY_ID: Int = 1

    override fun loadInitalData(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun openGallery(): Observable<Unit> = btnSettingsChangeImage.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override val changePicture: BehaviorSubject<SettingsIntent.ChangePicture> = BehaviorSubject.create()

    override fun changeColor(): Observable<Unit> = btnSettingsChangeColors.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun changeStatus(): Observable<Unit> = btnSettingsChangeStatus.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun changeName(): Observable<Unit> = btnSettingsChangeName.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_settings)

        supportActionBar!!.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK){
            val image: Uri = data!!.data

            CropImage.activity(image)
                .setAspectRatio(1,1)
                .start(this)
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)

            if(resultCode == Activity.RESULT_OK){
                val resultUri = result.uri

                val thumbFile = File(resultUri.path)

                //compress the image
                val thumbBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                changePicture.onNext(SettingsIntent.ChangePicture(resultUri))

            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Log.d("Error cropping image ", result.error.toString())
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun createPresenter(): SettingsPresenter {
        return SettingsPresenter(interactor, settingsCoordinator::openGallery ,settingsCoordinator::openChangeColor,
            settingsCoordinator::openChangeStatus, settingsCoordinator::openChangeName)
    }

    override fun render(state: SettingsViewState) {
        if(state.isLoading){
            //show progress bar
            progressBarSettings.visibility = View.VISIBLE
        }else{
            progressBarSettings.visibility = View.INVISIBLE
        }

        if(state.user != null){
            displayUserData(state.user)
        }

        if(!state.successfulMessage.isNullOrEmpty()){
            Toast.makeText(this, state.successfulMessage, Toast.LENGTH_SHORT).show()
        }

        if(!state.errorMessage.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserData(users: Users){
        if(TextUtils.isEmpty(users.status)){
            tvSettingsStatus.text = "No status"
        }
        else{
            tvSettingsStatus.text = users.status
        }

        if(TextUtils.isEmpty(users.displayName)){
            tvSettingsDisplayName.text = "No name to display"
        }
        else{
            tvSettingsDisplayName.text = users.displayName
        }

        Picasso.with(applicationContext)
            .load(users.image)
            .placeholder(R.drawable.profile_img)
            .into(settingsProfileID)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
