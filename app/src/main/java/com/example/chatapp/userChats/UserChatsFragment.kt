package com.example.chatapp.userChats


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.ChatAppApplication

import com.example.chatapp.R
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.UsersCoordinator
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UserChatsFragment : MviFragment<UserChatsView, UserChatsPresenter>(), UserChatsView {

    @Inject
    lateinit var interactor: UserChatsInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var usersCoordinator: UsersCoordinator

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    private var userChatAdapter: UserChatsAdapter? = null

    private var dialog: AlertDialog? = null

    override val loadUsers: PublishSubject<Unit> = PublishSubject.create()

    override val openDialog: PublishSubject<UserChatsIntent.OpenDialog> = PublishSubject.create()

    override val openChat: PublishSubject<Unit> = PublishSubject.create()

    override fun createPresenter(): UserChatsPresenter {
        return UserChatsPresenter(interactor, usersCoordinator::openChat)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        navigator.fragment = this
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onStart() {
        super.onStart()
        loadUsers.onNext(Unit)
    }

    override fun onAttach(context: Context) {
        (activity?.application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onAttach(context)
    }

    override fun render(state: UserChatsViewState) {
        if(state.isLoading){
            //show progress bar
        }

        if(state.chatUsers.isEmpty()){
            //show no user message
        }else{
            showUsers(state.chatUsers)
        }

        if(state.shouldOpenDialog){
            showDialog(state.userId!!)
        }else{
            hideDialog()
        }

    }

    private fun showUsers(users: List<Users>){
        userChatAdapter = UserChatsAdapter(context!!, users)
        userChatAdapter?.openDialog = openDialog

        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.adapter = userChatAdapter
    }

    private fun showDialog(userId: String){
        if(dialog == null){
            val options = arrayOf("View Message")
            dialog = AlertDialog.Builder(context)
                .setTitle("Select Options")
                .setItems(options){dialog, i->
                    if(i == 0){
                        firebaseRepository.receivingUserId = userId
                        openChat.onNext(Unit)
                    }
                }.create()
        }
        dialog?.show()
    }

    private fun hideDialog(){
        if(dialog != null && dialog?.isShowing!!){
            dialog?.dismiss()
        }
    }

}
