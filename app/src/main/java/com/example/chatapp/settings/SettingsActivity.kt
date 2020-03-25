package com.example.chatapp.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.format.Time
import android.view.View
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.SettingsCoordinator
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingsActivity : MviActivity<SettingsView, SettingsPresenter>(), SettingsView {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var settingsCoordinator: SettingsCoordinator

    @Inject
    lateinit var interactor: SettingsInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_settings)

        supportActionBar!!.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun createPresenter(): SettingsPresenter {
        return SettingsPresenter(interactor, settingsCoordinator::openChangeColor, settingsCoordinator::openChangeStatus, settingsCoordinator::openChangeName)
    }

    override fun loadInitalData(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun changePicture(): Observable<Unit> = btnSettingsChangeImage.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun changeColor(): Observable<Unit> = btnSettingsChangeColors.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun changeStatus(): Observable<Unit> = btnSettingsChangeStatus.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun changeName(): Observable<Unit> = btnSettingsChangeName.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun render(state: SettingsViewState) {

        if(state.isLoading){
            //show progress bar
            progressBarSettings.visibility = View.VISIBLE
        }else{
            progressBarSettings.visibility = View.INVISIBLE
        }

        if(state.user != null){
            dispalyUserData(state.user)
        }
    }

    private fun dispalyUserData(users: Users){
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
}
