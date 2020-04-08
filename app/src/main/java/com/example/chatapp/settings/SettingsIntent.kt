package com.example.chatapp.settings

import android.graphics.Bitmap
import android.net.Uri

sealed class SettingsIntent {

    data class ChangePicture(val resultUri: Uri?): SettingsIntent()
}