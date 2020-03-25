package com.example.chatapp.color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_color.*
import javax.inject.Inject

class ColorActivity : MviActivity<ColorView, ColorPresenter>(), ColorView {

    @Inject
    lateinit var interactor: ColorInteractor

    override val changeColor: PublishSubject<ColorIntent.ChangeColor> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color)

        supportActionBar?.title = "Choose a color"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        yellowColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#f5f7b5", "yellow"))
        }

        pinkColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#fcd4f6", "pink"))
        }

        blueColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#d4e3fc", "blue"))
        }

        redColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#ff7070", "red"))
        }

        greenColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#bdffc7", "green"))
        }

        whiteColor.setOnClickListener {
            changeColor.onNext(ColorIntent.ChangeColor("#ffffff", "white"))
        }
    }

    override fun createPresenter(): ColorPresenter {
        return ColorPresenter(interactor)
    }

    override fun render(state: ColorViewState) {
        if(!state.successfulMessage.isNullOrEmpty()){
            Toast.makeText(this, state.successfulMessage, Toast.LENGTH_SHORT).show()
        }

        if(!state.errorMessage.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
