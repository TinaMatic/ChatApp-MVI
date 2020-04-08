package com.example.chatapp.createAccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.LoginCoordinator
import com.example.chatapp.coordinator.MainCoordinator
import com.example.chatapp.coordinator.Navigator
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class CreateAccountActivity : MviActivity<CreateAccountView, CreateAccountPresenter>(), CreateAccountView {

    @Inject
    lateinit var interactor: CreateAccountInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var loginCoordinator: LoginCoordinator

    override fun loadPage(): Observable<Unit> = Observable.fromCallable { Unit }

    override fun createAccount(): Observable<CreateAccountIntent.CreateAccount> =
        btnAccountCreate.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
            .map { CreateAccountIntent.CreateAccount(etAccountDisplayName.text.toString(), etAccountEmail.text.toString(), etAccountPassword.text.toString()) }

    override fun createPresenter(): CreateAccountPresenter {
        return CreateAccountPresenter(interactor, loginCoordinator::openDashboard)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        navigator.activity = this
    }

    override fun render(state: CreateAccountViewState) {

        if(state.isLoading){
            //show progress bar
            progressBarCA.visibility = View.VISIBLE
        }else{
            progressBarCA.visibility = View.INVISIBLE
        }

        if(!state.nameMessageError.isNullOrEmpty()){
            Toast.makeText(this, state.nameMessageError, Toast.LENGTH_SHORT).show()
        }

        if(!state.emailMessageError.isNullOrEmpty()){
            Toast.makeText(this, state.emailMessageError, Toast.LENGTH_SHORT).show()
        }

        if(!state.passwordMessageError.isNullOrEmpty()){
            Toast.makeText(this, state.passwordMessageError, Toast.LENGTH_SHORT).show()
        }

        if(!state.creataAccountFailedError.isNullOrEmpty()){
            Toast.makeText(this, state.creataAccountFailedError, Toast.LENGTH_SHORT).show()
        }
    }
}
