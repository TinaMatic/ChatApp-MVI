package com.example.chatapp.userChats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.models.Users
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.users_row.view.*

class UserChatsAdapter (val context: Context, val users: List<Users>): RecyclerView.Adapter<UserChatsAdapter.ViewHolder>(){

    var openDialog: PublishSubject<UserChatsIntent.OpenDialog> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.users_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(users[position])

        holder.itemView.setOnClickListener {
            openDialog.onNext(UserChatsIntent.OpenDialog(users[position].userId!!))
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(user: Users){
            itemView.tvUserName.text = user.displayName
            itemView.tvUserStatus.text = user.status

            Picasso.with(context)
                .load(user.image)
                .placeholder(R.drawable.profile_img)
                .into(itemView.civUsersProfile)
        }
    }
}