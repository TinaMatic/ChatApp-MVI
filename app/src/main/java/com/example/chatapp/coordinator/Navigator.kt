package com.example.chatapp.coordinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatapp.createAccount.CreateAccountActivity
import com.example.chatapp.dashboard.DashboardActivity
import com.example.chatapp.login.LoginActivity
import com.example.chatapp.main.MainActivity
import com.example.chatapp.settings.SettingsActivity

class Navigator {

    var activity: AppCompatActivity? = null
    var fragment: Fragment? = null

    fun showLoginScreen(){
        val intent = Intent(activity, LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
    }

    fun showCreateAccountScreen(){
        val intent = Intent(activity, CreateAccountActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
    }

    fun showDashboardScreen(){
        val intent = Intent(activity, DashboardActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
    }

    fun showSettingsScreen(){
        val intent = Intent(activity, SettingsActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showMainScreen(){
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
    }
}