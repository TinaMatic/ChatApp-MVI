package com.example.chatapp

import android.app.Activity
import android.app.Application
import com.example.chatapp.di.ApplicationModule
import com.example.chatapp.di.ChapAppComponent
import com.example.chatapp.di.ChatAppModule
import com.example.chatapp.di.DaggerChapAppComponent
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class ChatAppApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: ChapAppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerChapAppComponent.builder()
            .application(this)
            .applicationModule(ApplicationModule(applicationContext))
            .chatAppModule(ChatAppModule())
            .build()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    fun getChatAppComponent(): ChapAppComponent{
        return appComponent
    }
}