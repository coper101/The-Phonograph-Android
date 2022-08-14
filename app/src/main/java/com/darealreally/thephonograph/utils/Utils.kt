package com.darealreally.thephonograph.utils

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.DateFormat

inline fun <reified T> load(
    context: Context,
    fileName: String
): List<T> {
    // encapsulate the object to evaluated by json
    val typeToken = object : TypeToken<List<T>>() {}.type

    val jsonData = context.applicationContext.assets
        .open(fileName)
        .bufferedReader()
        .readText()

    val items = GsonBuilder()
        .setDateFormat(DateFormat.FULL)
        .create()
        .fromJson<List<T>>(jsonData, typeToken)

    Log.d("Utils", "content: $jsonData")
    Log.d("Utils", "items: $items")
    return items.toList()
}

fun Int.toTimeFormat(): String {
    val min = (this % 3600) / 60
    val sec = this % 60
    return String.format("%2d:%02d", min, sec)
}