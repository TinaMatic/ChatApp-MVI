package com.example.chatapp.dashboard

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.coordinator.DashboardCoordinator
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_dashboard.*
import javax.inject.Inject

class DashboardActivity : MviActivity<DashboardView, DashboardPresenter>(), DashboardView {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var dashboardCoordinator: DashboardCoordinator

    @Inject
    lateinit var interactor: DashboardInteractor

    override val logout: PublishSubject<Unit> = PublishSubject.create()

    override val openSettings: PublishSubject<Unit> = PublishSubject.create()

    override fun createPresenter(): DashboardPresenter {
        return DashboardPresenter(interactor, dashboardCoordinator::openSettings, dashboardCoordinator::openMainScreen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        navigator.activity = this
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.title = "Dashboard"

        val sectionAdapter = SectionPagerAdapter(supportFragmentManager)
        dashboardViewPager.adapter = sectionAdapter
        mainTabs.setupWithViewPager(dashboardViewPager)
        mainTabs.setTabTextColors(Color.WHITE, Color.BLUE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item != null){
            if(item.itemId == R.id.logut){
                logout.onNext(Unit)
            }
            if(item.itemId == R.id.settings){
                openSettings.onNext(Unit)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun render(state: DashboardViewState) {
        if(state.isLoading){
            //show progress bar
        }

        if(!state.errorMessage.isNullOrEmpty()){
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
