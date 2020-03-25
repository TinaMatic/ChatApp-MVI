package com.example.chatapp.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.chatapp.chat.ChatFragment
import com.example.chatapp.users.UsersFragment

class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            //first tab
            0 ->{
                 UsersFragment()
            }
            //second tab
            1 ->{
                 ChatFragment()
            }
            else ->{
                null!!
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position){
            0->{
                return "USERS"
            }
            1->{
                return "CHATS"
            }
        }

        return super.getPageTitle(position)
    }
}