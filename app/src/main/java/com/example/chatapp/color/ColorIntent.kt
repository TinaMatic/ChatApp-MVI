package com.example.chatapp.color

sealed class ColorIntent {

    data class ChangeColor(val color: String, val colorName:String): ColorIntent()
}