package com.example.chatapp.chat

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.models.Messages
import com.example.chatapp.models.Users
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_message.view.*

class ChatAdapter (val context: Context, val users: List<Users>, val messages: List<Messages>, val currentUserId: String):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private var itemClickedListener: OnItemClickListener? = null

    var openDialog: PublishSubject<ChatIntent.OpenDialog> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItem(messages[position])

        holder.itemView.setOnClickListener {
            itemClickedListener?.onItemClick(messages[position].messageId!!, messages[position].text)
            openDialog.onNext(ChatIntent.OpenDialog(messages[position].sendingId))
        }
    }

    fun setItemClickListener(clickListener: OnItemClickListener?){
        this.itemClickedListener = clickListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var currentUserPosition: Int = 0
        var receivingUserPosition: Int = 1

        fun bindItem(message: Messages){
            //get the current user postiton
            if(currentUserId.equals(users[1].userId)){
                currentUserPosition = 1
                receivingUserPosition = 0
            }
            //check if the current user is sending user and put messages the right
            if(message.sendingId.equals(currentUserId)){
                itemView.senderImageView.visibility = View.VISIBLE
                itemView.receiverImageView.visibility = View.GONE
                itemView.tvMessage.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)
                itemView.tvReceiverName.visibility = View.GONE
                itemView.tvSenderName.visibility = View.VISIBLE

                itemView.tvMessage.text = message.text

                itemView.cvMessage.setBackgroundColor(Color.parseColor(users[currentUserPosition].color))
                itemView.tvSenderName.text = "${users[currentUserPosition].displayName} wrote..."
                Picasso.with(context)
                    .load(users[currentUserPosition].image)
                    .placeholder(R.drawable.profile_img)
                    .into(itemView.senderImageView)


            }else{
                itemView.cvMessage.isClickable = false
                itemView.senderImageView.visibility = View.GONE
                itemView.receiverImageView.visibility = View.VISIBLE
                itemView.tvMessage.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                itemView.tvReceiverName.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                itemView.tvSenderName.visibility = View.GONE

                itemView.tvMessage.text = message.text
                itemView.cvMessage.setBackgroundColor(Color.parseColor(users[receivingUserPosition].color))
                itemView.tvReceiverName.text = "${users[receivingUserPosition].displayName} wrote..."
                Picasso.with(context)
                    .load(users[receivingUserPosition].image)
                    .placeholder(R.drawable.profile_img)
                    .into(itemView.receiverImageView)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(messageId: String, message: String)
    }
}