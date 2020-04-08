package com.example.chatapp.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.LoginCoordinator
import com.example.chatapp.coordinator.Navigator
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginActivity : MviActivity<LoginView, LoginPresenter>(), LoginView {

    @Inject
    lateinit var interacor: LoginInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var loginCoordinator: LoginCoordinator


    override fun loadPageIntent(): Observable<Unit> = Observable.fromCallable { Unit }

    override fun login(): Observable<LoginIntent.Login> =
        btnLogin.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
            .map { LoginIntent.Login(etLoginEmail.text.toString(), etLoginPassword.text.toString()) }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter(interacor, loginCoordinator::openDashboard)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_login)
    }

    override fun render(state: LoginViewState) {

        if(state.isLoading){
            //show progress bar
            progressBarLogin.visibility = View.VISIBLE
        }else{
            progressBarLogin.visibility = View.INVISIBLE
        }

        if(!state.errorMessageEmail.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessageEmail, Toast.LENGTH_SHORT).show()
        }

        if(!state.errorMessagePassword.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessagePassword, Toast.LENGTH_SHORT).show()
        }

        if(!state.loginFailedMessage.isNullOrEmpty()){
            Toast.makeText(this, state.loginFailedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
