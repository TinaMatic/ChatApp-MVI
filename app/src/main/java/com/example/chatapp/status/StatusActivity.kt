package com.example.chatapp.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_status.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StatusActivity : MviActivity<StatusView, StatusPresenter>(), StatusView {

    @Inject
    lateinit var interactor: StatusInteractor

    override fun loadInitialData(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun changeStatus(): Observable<StatusIntent.ChangeStatus> =
        btnStatusUpdate.clicks().throttleFirst(500, TimeUnit.MILLISECONDS).map {
            StatusIntent.ChangeStatus(etStatusUpdate.text.toString())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportActionBar!!.title = "Status"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun createPresenter(): StatusPresenter {
        return StatusPresenter(interactor)
    }

    override fun render(state: StatusViewState) {
        if(!state.status.isNullOrEmpty()){
            etStatusUpdate.setText(state.status)
        }else{
            etStatusUpdate.setText("Enter Your New Status")
        }

        if(!state.successfulMessage.isNullOrEmpty()){
            Toast.makeText(this, state.successfulMessage, Toast.LENGTH_SHORT).show()
        }

        if(!state.errorMessage.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
