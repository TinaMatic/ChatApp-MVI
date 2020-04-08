package com.example.chatapp.users


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
import com.example.chatapp.chat.ChatIntent
import com.example.chatapp.chat.ChatInteractor
import com.example.chatapp.coordinator.Navigator
import com.example.chatapp.coordinator.UsersCoordinator
import com.example.chatapp.data.FirebaseRepository
import com.example.chatapp.models.Users
import com.hannesdorfmann.mosby3.mvi.MviFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : MviFragment<UsersView, UsersPresenter>(), UsersView {

    @Inject
    lateinit var interactor: UsersInteractor

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var usersCoordinator: UsersCoordinator

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

    private var usersAdapter: UsersAdapter? = null

    private var dialog: AlertDialog? = null

    override val loadInitialData: PublishSubject<Unit> = PublishSubject.create()
    override val openDialog: PublishSubject<UsersIntent.OpenDialog> = PublishSubject.create()
    override val openProfile: PublishSubject<Unit> = PublishSubject.create()
    override val openChat: PublishSubject<Unit> = PublishSubject.create()

    override fun createPresenter(): UsersPresenter {
        return UsersPresenter(interactor, usersCoordinator::openProfile, usersCoordinator::openChat)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        navigator.fragment = this
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ne udje u prezenter
        loadInitialData.onNext(Unit)

    }

    override fun onAttach(context: Context) {
        (activity?.application as ChatAppApplication).getChatAppComponent().inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        loadInitialData.onNext(Unit)
    }

    fun showUsers(users: List<Users>){
        usersAdapter = UsersAdapter(context!!, users)
        usersAdapter?.openDialog = openDialog

        userRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.adapter = usersAdapter
    }

    override fun render(state: UsersViewState) {
        if(state.isLoading){
            //show progressbar
        }

        if(state.users.isEmpty()){
            //show no user message
        }else{
            showUsers(state.users)
        }

        if(state.showDialog){
           showDialog(state.userId!!)
        }else{
            closeDialog()
        }
    }

    fun showDialog(userId: String){
        if(dialog == null){
            val options = arrayOf("Open Profile", "Send Message")
            dialog = AlertDialog.Builder(context)
                .setTitle("Select Options")
                .setItems(options){dialog, i->
                if(i == 0){
                    firebaseRepository.receivingUserId = userId
                    openProfile.onNext(Unit)
                }else{
                    firebaseRepository.receivingUserId = userId
                    openChat.onNext(Unit)
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

}
