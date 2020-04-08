package com.example.chatapp.profile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.UsersCoordinator
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileActivity : MviActivity<ProfileView, ProfilePresenter>(), ProfileView {

    @Inject
    lateinit var interactor: ProfileInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var usersCoordinator: UsersCoordinator

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    override fun loadData(): Observable<ProfileIntent.LoadUser> {
        return Observable.just(ProfileIntent.LoadUser(firebaseRepository.receivingUserId!!))
    }

    override fun openChat(): Observable<Unit> =
        btnProfileSendMessage.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter(interactor, usersCoordinator::openChat)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun render(state: ProfileViewState) {

        if(state.isLoading){
            //show progress bar
        }

        if(state.user != null){
            displayUser(state.user)
        }
    }

    private fun displayUser(user: Users){
        profileBackground.setBackgroundColor(Color.parseColor(user.color))
        tvProfileName.text = user.displayName
        tvProfileStatus.text = user.status

        Picasso.with(this)
            .load(user.image)
            .placeholder(R.drawable.happy_woman)
            .into(ivProfilePicture)
    }
}
