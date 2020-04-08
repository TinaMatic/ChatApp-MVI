package com.example.chatapp.chat

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ChatView: MvpView {

    /**
     * Load messages
     */
    fun loadMessages(): Observable<Unit>

    /**
     * Load users
     */
    fun loadUsers(): Observable<Unit>

    /**
     * Send messages
     */
    fun sendMessage(): Observable<ChatIntent.SendMessage>

    /**
     * Open dialog
     */
    val openDialog: PublishSubject<ChatIntent.OpenDialog>

    /**
     * Edit message
     */
    fun editMessage(): Observable<ChatIntent.EditMessage>

    /**
     * Delete message
     */
    val deleteMessage: PublishSubject<ChatIntent.DeleteMessage>

    /**
     * Render the state of the UI
     */
    fun render(state: ChatViewState)
}