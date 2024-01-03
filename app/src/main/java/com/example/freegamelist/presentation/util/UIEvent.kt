package com.example.freegamelist.presentation.util

sealed class UIEvent{
    data class ShowSnackBar(val message:String):UIEvent()
}
