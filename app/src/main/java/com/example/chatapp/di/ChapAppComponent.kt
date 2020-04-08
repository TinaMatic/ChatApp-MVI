package com.example.chatapp.di

import android.app.Application
import com.example.chatapp.chat.ChatActivity
import com.example.chatapp.userChats.UserChatsFragment
import com.example.chatapp.color.ColorActivity
import com.example.chatapp.createAccount.CreateAccountActivity
import com.example.chatapp.dashboard.DashboardActivity
import com.example.chatapp.login.LoginActivity
import com.example.chatapp.main.MainActivity
import com.example.chatapp.name.NameActivity
import com.example.chatapp.profile.ProfileActivity
import com.example.chatapp.settings.SettingsActivity
import com.example.chatapp.status.StatusActivity
import com.example.chatapp.users.UsersFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class, ChatAppModule::class])
interface ChapAppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: CreateAccountActivity)
    fun inject(activity: DashboardActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: ColorActivity)
    fun inject(activity: StatusActivity)
    fun inject(activity: NameActivity)
    fun inject(activity: ChatActivity)
    fun inject(activity: ProfileActivity)

    fun inject(fm: UserChatsFragment)
    fun inject(fm: UsersFragment)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun chatAppModule(chatAppModule: ChatAppModule): Builder

        fun build(): ChapAppComponent
    }
}