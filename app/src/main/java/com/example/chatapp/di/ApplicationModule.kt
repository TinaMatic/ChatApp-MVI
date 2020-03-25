package com.example.chatapp.di

import android.content.Context
import android.content.res.Resources
import com.example.chatapp.coordinator.Navigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    private val navigator = Navigator()

    @Provides
    @Singleton
    fun provideContext(): Context{
        return context
    }


    @Provides
    @Singleton
    fun provideResources(): Resources{
        return context.resources
    }

    @Provides
    @Singleton
    fun provideNavigator(): Navigator{
        return navigator
    }
}