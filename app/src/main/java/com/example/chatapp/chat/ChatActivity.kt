package com.example.chatapp.chat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.ChatAppApplication
import com.example.chatapp.R
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Messages
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custom_bar_image.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatActivity : MviActivity<ChatView, ChatPresenter>(), ChatView, ChatAdapter.OnItemClickListener{

    @Inject
    lateinit var interactor: ChatInteractor

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    var chatAdapter: ChatAdapter? = null

    private var dialog: AlertDialog? = null

    var clickedMessageId: String? = null

    var clickedMessageText: String? = null

    override fun loadMessages(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun loadUsers(): Observable<Unit> {
        return Observable.fromCallable { Unit }
    }

    override fun sendMessage(): Observable<ChatIntent.SendMessage> =
        btnSend.clicks().throttleFirst(500, TimeUnit.MILLISECONDS).map {
            val message = Messages(null, firebaseRepository.currentUser!!.uid, firebaseRepository.receivingUserId!!,
                etMessage.text.toString(), null, null)
            ChatIntent.SendMessage(message)
        }.doOnNext {
            etMessage.setText("")
        }

    override val openDialog: PublishSubject<ChatIntent.OpenDialog> = PublishSubject.create()

    override fun editMessage(): Observable<ChatIntent.EditMessage> =
        btnEdit.clicks().throttleFirst(500, TimeUnit.MILLISECONDS).map {
            ChatIntent.EditMessage(etMessage.text.toString(), clickedMessageId!!)
        }.doOnNext {
            chatAdapter?.notifyDataSetChanged()
        }

    override val deleteMessage: PublishSubject<ChatIntent.DeleteMessage> = PublishSubject.create()

    override fun createPresenter(): ChatPresenter {
        return ChatPresenter(interactor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.title = "Chat"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowCustomEnabled(true)


        btnAddMessage.setOnClickListener {
            showKeyboard(etMessage)
        }

        btnCancel.setOnClickListener {
            etMessage.setText("")
            btnSend.visibility = View.VISIBLE
            btnEdit.visibility = View.GONE

            btnCancel.visibility = View.GONE
            btnAddMessage.visibility = View.VISIBLE
        }

        chatAdapter?.notifyDataSetChanged()
    }

    override fun render(state: ChatViewState) {
        if (state.isLoading){
            //show progress bar
        }

        if(!state.messages.isNullOrEmpty() and !state.users.isNullOrEmpty()){
            showMessages(state.messages, state.users)
        }

        if(state.showDialog){
            showDialog()
        }else{
            closeDialog()
        }

        if(!state.successfulEditMessage.isNullOrEmpty()){
            Toast.makeText(this, state.successfulEditMessage, Toast.LENGTH_SHORT).show()
            etMessage.setText("")

            btnSend.visibility = View.VISIBLE
            btnEdit.visibility = View.GONE

            btnAddMessage.visibility = View.VISIBLE
            btnCancel.visibility = View.GONE

            hideKeyboard(etMessage)
        }

        if(!state.errorEditMessage.isNullOrEmpty()){
            Toast.makeText(this , state.errorEditMessage, Toast.LENGTH_SHORT).show()
        }

    }

    fun showMessages(messages: List<Messages>, users: List<Users>){
        var receivingUserName: String? = null
        var userImage: String? = null

        //not working if there are no messages
        if (firebaseRepository.receivingUserId.equals(users[0].userId)){
            receivingUserName = users[0].displayName
            userImage = users[0].image
        }else{
            receivingUserName = users[1].displayName
            userImage = users[1].image
        }

        chatAdapter = ChatAdapter(applicationContext, users, messages, firebaseRepository.currentUser!!.uid)
        chatAdapter?.openDialog = openDialog
        chatAdapter?.setItemClickListener(this)

        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.stackFromEnd = true

        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarView = inflater.inflate(R.layout.custom_bar_image, null)
        actionBarView.tvCustomBarName.text = receivingUserName
        Picasso.with(this)
            .load(userImage)
            .placeholder(R.drawable.profile_img)
            .into(actionBarView.ciCustomBarImage)

        supportActionBar?.customView = actionBarView

        chatRecyclerView.layoutManager = mLinearLayoutManager
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.adapter = chatAdapter

        chatAdapter?.notifyDataSetChanged()
    }

    fun showDialog(){
        if(dialog == null){
            val options = arrayOf("Edit", "Delete")
            dialog = AlertDialog.Builder(this)
                .setTitle("Choose an option")
                .setItems(options){dialog,i->
                    if(i == 0){
                        //edit message
                        btnSend.visibility = View.GONE
                        btnEdit.visibility = View.VISIBLE

                        btnCancel.visibility = View.VISIBLE
                        btnAddMessage.visibility = View.GONE

                        etMessage.setText(clickedMessageText)

                    } else if (i == 1){
                        //delete message
                        deleteMessage.onNext(ChatIntent.DeleteMessage(clickedMessageId!!))
                        chatAdapter?.notifyDataSetChanged()
                    }
                }.create()
        }
        dialog?.show()
    }

    fun closeDialog(){
        if(dialog != null && dialog?.isShowing!!){
            dialog?.dismiss()
        }
    }

    private fun showKeyboard(view: View){
        if(view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(view: View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onItemClick(messageId: String, message: String) {
        clickedMessageId = messageId
        clickedMessageText = message
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
