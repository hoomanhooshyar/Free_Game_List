package com.example.freegamelist.presentation.util

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toFormattedDate():String{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(this)
    val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return outputFormat.format(date!!)
}