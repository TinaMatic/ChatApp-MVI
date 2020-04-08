package com.example.chatapp.users

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

class UsersAdapter (val context: Context, val users: List<Users>): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    var openDialog: PublishSubject<UsersIntent.OpenDialog> = PublishSubject.create()

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
            openDialog.onNext(UsersIntent.OpenDialog(users[position].userId!!))
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(users: Users){
            itemView.tvUserName.text = users.displayName
            itemView.tvUserStatus.text = users.status

            Picasso.with(context)
                .load(users.image)
                .placeholder(R.drawable.profile_img)
                .into(itemView.civUsersProfile)
        }
    }

}