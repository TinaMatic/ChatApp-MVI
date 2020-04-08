package com.example.chatapp.coordinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatapp.chat.ChatActivity
import com.example.chatapp.color.ColorActivity
import com.example.chatapp.createAccount.CreateAccountActivity
import com.example.chatapp.dashboard.DashboardActivity
import com.example.chatapp.login.LoginActivity
import com.example.chatapp.main.MainActivity
import com.example.chatapp.name.NameActivity
import com.example.chatapp.profile.ProfileActivity
import com.example.chatapp.settings.SettingsActivity
import com.example.chatapp.status.StatusActivity

class Navigator {

    var activity: AppCompatActivity? = null
    var fragment: Fragment? = null

    val GALLERY_ID: Int = 1

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
        activity?.finish()
    }

    fun showSettingsScreen(){
        val intent = Intent(activity, SettingsActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showMainScreen(){
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }

    fun showGallery(){
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        activity?.startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
    }


    fun showChangeColorScreen(){
        val intent = Intent(activity, ColorActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showChangeStatusScreen(){
        val intent = Intent(activity, StatusActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showChangeNameScreen(){
        val intent = Intent(activity, NameActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showProfileScreen(){
        val intent = Intent(activity, ProfileActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showChatScreen(){
        val intent = Intent(activity, ChatActivity::class.java)
        activity?.startActivity(intent)
    }
}