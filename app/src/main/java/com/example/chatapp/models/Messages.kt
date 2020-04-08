package com.example.chatapp.models

import javax.inject.Inject

data class Messages @Inject constructor (val messageId: String?, val sendingId: String, val receivingId: String, val text: String,
                                        val receiverName: String?, val senderName: String?)