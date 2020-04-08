package com.example.chatapp.models

import java.util.*
import javax.inject.Inject

data class Users @Inject constructor (var userId: String?, var displayName: String?, var image: String?,
                                      var thumbImage: String?, var status: String?,
                                      var color: String?)