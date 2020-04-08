package com.example.chatapp.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.coordinator.MainCoordinator
import com.example.chatapp.R
import com.example.chatapp.coordinator.LoginCoordinator
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.createAccount.CreateAccountActivity
import com.example.chatapp.dashboard.DashboardActivity
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : MviActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var mainInteractor: MainInteractor

    @Inject
    lateinit var mainCoordinator: MainCoordinator

    @Inject
    lateinit var loginCoordinator: LoginCoordinator

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var firebaseRepository: FirebaseRepository


    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun loadPageIntent(): Observable<Unit> = Observable.fromCallable { Unit }

    override val isLoggedIn: PublishSubject<Unit> = PublishSubject.create()

    override fun openLogin(): Observable<Unit> =
        loginButtonMain.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun openCreateNewAccount(): Observable<Unit> = createActButton.clicks()

    override val openDashboard: PublishSubject<Unit> = PublishSubject.create()

    override fun createPresenter(): MainPresenter {
        return MainPresenter(mainInteractor, mainCoordinator::openLogin, mainCoordinator::openCreateAccount, loginCoordinator::openDashboard)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this

        setContentView(R.layout.activity_main)

        isLoggedIn.onNext(Unit)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
                firebaseAuth: FirebaseAuth ->

            user = firebaseAuth.currentUser

            if(user != null){
                openDashboard.onNext(Unit)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if(mAuth != null){
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun render(state: MainViewState) {
        if(state.isLoading){
            //show progress bar
            progressBarMain.visibility = View.VISIBLE
        }else{
            progressBarMain.visibility = View.INVISIBLE
        }
        if (!state.loginMessage.isNullOrEmpty()){
            Toast.makeText(this, state.loginMessage, Toast.LENGTH_SHORT).show()
        }

    }
}
