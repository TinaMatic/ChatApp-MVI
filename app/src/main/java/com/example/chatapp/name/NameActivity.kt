package com.example.chatapp.name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.SettingsCoordinator
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_name.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NameActivity : MviActivity<NameView, NamePresenter>(), NameView{

    @Inject
    lateinit var interactor: NameInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var settingsCoordinator: SettingsCoordinator

    override fun loadInitialName(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun changeName(): Observable<NameIntent.ChangeName> =
        btnNameUpdate.clicks().throttleFirst(500, TimeUnit.MILLISECONDS).map {
            NameIntent.ChangeName(etNameUpdate.text.toString())
        }

    override fun createPresenter(): NamePresenter {
        return NamePresenter(interactor, settingsCoordinator::openSettings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_name)

        supportActionBar!!.title = "Display Name"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun render(state: NameViewState) {

        if(!state.name.isNullOrEmpty()){
            etNameUpdate.setText(state.name)
        }

        if(!state.successfulNameChange.isNullOrEmpty()){
            Toast.makeText(this, state.successfulNameChange, Toast.LENGTH_SHORT).show()
        }

        if(!state.error.isNullOrEmpty()){
            Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
